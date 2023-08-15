package com.el.yello.presentation.util

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BillingManager(private val activity: Activity, private val callback: BillingCallback) {

    // 결제 관련 업데이트 수신 리스너
    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                confirmPurchase(purchase)
            }
        } else {
            callback.onFailure(billingResult.responseCode)
        }
    }

    // 결제 라이브러리 통신 위한 BillingClient 초기화
    private val billingClient = BillingClient.newBuilder(activity)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    // BillingClient을 결제 라이브러리에 연결
    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                // 연결 끊어졌을 때 로직 설정 (재시도)
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    callback.onBillingConnected()
                } else {
                    callback.onFailure(billingResult.responseCode)
                }
            }
        })
    }

    // 결과로 받을 상품 정보들에 대한 처리
    fun getProductDetails(
        resultBlock: (List<ProductDetails>) -> Unit = {}
    ) {
        val productList =
            listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId("up_basic_sub")
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            )
        val params = QueryProductDetailsParams.newBuilder().setProductList(productList)

        billingClient.queryProductDetailsAsync(params.build()) { _, productDetailsList ->
            CoroutineScope(Dispatchers.Main).launch {
                resultBlock(productDetailsList)
            }
        }
    }

    // 구매 흐름 시작
    fun purchaseProduct(productDetails: ProductDetails, selectedOfferIndex: Int) {
        val offerToken =
            productDetails.subscriptionOfferDetails?.get(selectedOfferIndex)?.offerToken

        val productDetailsParamsList =
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken ?: "")
                    .build()
            )
        val billingFlowParams =
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()

        val responseCode = billingClient.launchBillingFlow(activity, billingFlowParams).responseCode
        if (responseCode != BillingClient.BillingResponseCode.OK) {
            callback.onFailure(responseCode)
        }
    }

    // 구독 여부 확인 후 구매 처리
    fun checkPurchased(billingType: String, resultBlock: (Purchase?) -> Unit) {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(billingType)
                .build()
        ) { _, purchaseResult ->
            CoroutineScope(Dispatchers.Main).launch {
                for (purchase in purchaseResult) {
                    if (purchase.isAcknowledged && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        return@launch resultBlock(purchase)
                    }
                }
                return@launch resultBlock(null)
            }
        }
    }

    // 구매 여부 확인
    fun confirmPurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            // 구매를 완료 했지만 확인이 되지 않은 경우 확인 처리
            val ackPurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)

            CoroutineScope(Dispatchers.IO).launch {
                billingClient.acknowledgePurchase(ackPurchaseParams.build()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                            callback.onSuccess(purchase)
                        } else {
                            callback.onFailure(it.responseCode)
                        }
                    }
                }
            }
        }
    }

    // 구매 재확인
    fun confirmPurchaseAgain(billingType: String) {
        if (billingClient.isReady) {
            billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(billingType)
                    .build()
            ) { _, purchases ->
                for (purchase in purchases) {
                    if (!purchase.isAcknowledged && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        confirmPurchase(purchase)
                    }
                }
            }
        }
    }
}