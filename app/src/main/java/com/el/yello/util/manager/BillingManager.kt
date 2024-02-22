package com.el.yello.util.manager

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClient.ProductType.INAPP
import com.android.billingclient.api.BillingClient.ProductType.SUBS
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber

class BillingManager(private val activity: Activity, private val callback: BillingCallback) {

    private val _isPurchasing = MutableStateFlow(false)
    val isPurchasing: StateFlow<Boolean> = _isPurchasing

    // 결제 시 작동하는 리스너
    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            setIsPurchasing(true)
            for (purchase in purchases) confirmPurchase(purchase)
        } else {
            callback.onFailure(billingResult.responseCode)
        }
    }

    // 결제 라이브러리 통신 위한 BillingClient 초기화
    val billingClient =
        BillingClient.newBuilder(activity.applicationContext).setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()

    // BillingClient을 결제 라이브러리에 연결
    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Timber.d("Billing Service Disconnected")
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

    // 로딩 로티뷰 돌리기 위한 상태값 저장 초기화
    fun setIsPurchasing(boolean: Boolean) {
        _isPurchasing.value = boolean
    }

    // 결과로 받을 상품 정보 받아오기
    suspend fun getProductDetails(resultBlock: (List<ProductDetails>) -> Unit = {}) {
        runBlocking {
            val subsProducts = withContext(Dispatchers.IO) { getSubsProductDetails() }
            val inAppProducts = withContext(Dispatchers.IO) { getInAppProductDetails() }
            resultBlock(subsProducts + inAppProducts)
        }
    }

    private suspend fun getSubsProductDetails(): List<ProductDetails> {
        val subsProductList = ArrayList<Product>().apply {
            add(
                Product.newBuilder().setProductId(YELLO_PLUS).setProductType(SUBS).build()
            )
        }
        val subsProductDetailList = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(
                QueryProductDetailsParams.newBuilder().setProductList(subsProductList).build()
            ).productDetailsList
        } ?: listOf()
        return subsProductDetailList
    }

    private suspend fun getInAppProductDetails(): List<ProductDetails> {
        val inAppProductList = ArrayList<Product>().apply {
            listOf(YELLO_ONE, YELLO_TWO, YELLO_FIVE).forEach { productId ->
                add(
                    Product.newBuilder().setProductId(productId).setProductType(INAPP).build()
                )
            }
        }
        val inAppProductDetailList = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(
                QueryProductDetailsParams.newBuilder().setProductList(inAppProductList).build()
            ).productDetailsList
        } ?: listOf()
        return inAppProductDetailList
    }

    // 구매 진행
    fun purchaseProduct(selectedOfferIndex: Int, productDetails: ProductDetails) {
        val offerToken =
            productDetails.subscriptionOfferDetails?.get(selectedOfferIndex)?.offerToken

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails)
                .setOfferToken(offerToken ?: "").build()
        )
        val billingFlowParams =
            BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList)
                .build()

        val responseCode = billingClient.launchBillingFlow(activity, billingFlowParams).responseCode
        if (responseCode != BillingClient.BillingResponseCode.OK) {
            callback.onFailure(responseCode)
        }
    }

    // 구매 여부 확인
    private fun confirmPurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            // 구매를 완료 했지만 확인이 되지 않은 경우 확인 처리
            val ackPurchaseParams =
                AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken)

            CoroutineScope(Dispatchers.Main).launch {
                billingClient.acknowledgePurchase(ackPurchaseParams.build()) {
                    if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                        consumePurchase(purchase)
                    } else {
                        callback.onFailure(it.responseCode)
                    }
                }
            }
        }
    }

    // 소비성 아이템 소비 완료 표시 (재구매 위해)
    private fun consumePurchase(purchase: Purchase) {
        if (purchase.products[0] != YELLO_PLUS) {
            val consumeParams =
                ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()

            billingClient.consumeAsync(consumeParams) { billingResult, _ ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    checkConsumable(purchase)
                } else {
                    callback.onFailure(billingResult.responseCode)
                }
            }
        } else {
            // 구독 상품의 경우, 소비 필요 없으므로 바로 success 이동
            callback.onSuccess(purchase)
        }
    }

    // 구매 표시 안된 소비성 아이템 찾아 소비 완료 다시 실행, 모두 완료 확인 후 성공 설정
    private fun checkConsumable(purchasedItem: Purchase) {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(ProductType.INAPP).build()
        ) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (purchaseList.isNotEmpty()) {
                    purchaseList.forEach { purchase -> consumePurchase(purchase) }
                } else {
                    callback.onSuccess(purchasedItem)
                }
            } else {
                callback.onFailure(billingResult.responseCode)
            }
        }
    }
}