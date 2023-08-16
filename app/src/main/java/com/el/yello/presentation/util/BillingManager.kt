package com.el.yello.presentation.util

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.el.yello.presentation.pay.PayActivity.Companion.YELLO_FIVE
import com.el.yello.presentation.pay.PayActivity.Companion.YELLO_ONE
import com.el.yello.presentation.pay.PayActivity.Companion.YELLO_PLUS
import com.el.yello.presentation.pay.PayActivity.Companion.YELLO_TWO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingManager(private val activity: Activity, private val callback: BillingCallback) {

    // 결제 시 작동하는 리스너
    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                Log.d("sangho", "@@@ 1: ${purchases}")
                for (purchase in purchases) {
                    Log.d("sangho", "@@@ 2: ${purchase}")
                    confirmPurchase(purchase)
                }
            } else {
                callback.onFailure(billingResult.responseCode)
            }
        }

    // 결제 라이브러리 통신 위한 BillingClient 초기화
    private val billingClient = BillingClient.newBuilder(activity.applicationContext)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    // BillingClient을 결제 라이브러리에 연결
    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Log.d("sangho", "@@@ 3")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    callback.onBillingConnected()
                    Log.d("sangho", "@@@ 4: ${billingResult}")
                } else {
                    callback.onFailure(billingResult.responseCode)
                    Log.d("sangho", "@@@ 1: ${billingResult.responseCode}")
                }
            }
        })
    }

    // 결과로 받을 상품 정보들에 대한 처리
    suspend fun getProductDetails(
        resultBlock: (List<ProductDetails>) -> Unit = {}
    ) {
        val productDetailsList = mutableListOf<ProductDetails>()
        Log.d("sangho", "@@@ 6: ${productDetailsList}")

        val subProductList = ArrayList<Product>()
        subProductList.add(
            Product.newBuilder()
                .setProductId(YELLO_PLUS)
                .setProductType(ProductType.SUBS)
                .build()
        )

        val subParams = QueryProductDetailsParams.newBuilder().setProductList(subProductList)
        val subsList = withContext(Dispatchers.Main) {
            billingClient.queryProductDetails(subParams.build()).productDetailsList
        } ?: listOf()
        productDetailsList.addAll(0, subsList)

        val inAppProductList = ArrayList<Product>()
        val productInfoList = listOf(
            YELLO_ONE,
            YELLO_TWO,
            YELLO_FIVE
        )
        productInfoList.forEach { productId ->
            val product = Product.newBuilder()
                .setProductId(productId)
                .setProductType(ProductType.INAPP)
                .build()
            inAppProductList.add(product)
        }

        val inAppParams = QueryProductDetailsParams.newBuilder().setProductList(inAppProductList)
        val inAppList = withContext(Dispatchers.Main) {
            billingClient.queryProductDetails(inAppParams.build()).productDetailsList
        } ?: listOf()
        productDetailsList.addAll(productDetailsList.size, inAppList)
        Log.d("sangho", "@@@ 7: ${productDetailsList}")
        resultBlock(productDetailsList)
    }

    // 구매 흐름 시작
    fun purchaseProduct(selectedOfferIndex: Int, productDetails: ProductDetails) {
        val offerToken =
            productDetails.subscriptionOfferDetails?.get(selectedOfferIndex)?.offerToken

        val productDetailsParamsList =
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken ?: "")
                    .build()
            )
        Log.d("sangho", "@@@ 8: ${productDetailsParamsList}")
        val billingFlowParams =
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()

        val responseCode = billingClient.launchBillingFlow(activity, billingFlowParams).responseCode
        Log.d("sangho", "@@@ 9: ${responseCode}")
        if (responseCode != BillingClient.BillingResponseCode.OK) {
            callback.onFailure(responseCode)
        }
    }

    // 구매 여부 확인
    private fun confirmPurchase(purchase: Purchase) {
        Log.d("sangho", "@@@ 13")
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            // 구매를 완료 했지만 확인이 되지 않은 경우 확인 처리
            val ackPurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
            Log.d("sangho", "@@@ 14: ${ackPurchaseParams}")

            CoroutineScope(Dispatchers.Main).launch {
                billingClient.acknowledgePurchase(ackPurchaseParams.build()) {
                    if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.d("sangho", "@@@ 15: ${purchase}")
                        consumePurchase(purchase)
                        callback.onSuccess(purchase)
                    } else {
                        callback.onFailure(it.responseCode)
                    }
                }
            }
        }
    }

    // 소비성 아이템 소비 완료 표시 (재구매 위해)
    private fun consumePurchase(purchase: Purchase) {
        Log.d("sangho", "@@@ 16: ${purchase}")
        if (purchase.products[0] != YELLO_PLUS) {
            val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            Log.d("sangho", "@@@ 17: ${consumeParams}")
            billingClient.consumeAsync(consumeParams) { billingResult, _ ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d("sangho", "@@@ 18")
                    // 추가 완료 서버통신 ?
                    checkConsumable()
                }
            }
        }
    }

    // 구매 표시 미완료된 소비성 아이템 찾아 소비 완료로 변경
    fun checkConsumable() {
        Log.d("sangho", "@@@ 19")
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(ProductType.INAPP)
                .build()
        ) { billingResult, purchaseList ->
            Log.d("sangho", "@@@ 20: ${billingResult}")
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d("sangho", "@@@ 21: ${purchaseList}")
                purchaseList.forEach { purchase ->
                    Log.d("sangho", "@@@ 22: ${purchase}")
                    consumePurchase(purchase)
                }
            } else {
                // TODO: Service connection is disconnected.
            }
        }
    }
}