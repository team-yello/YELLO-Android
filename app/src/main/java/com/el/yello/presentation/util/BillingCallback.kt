package com.el.yello.presentation.util

import com.android.billingclient.api.Purchase

interface BillingCallback {
    fun onBillingConnected() // BillingClient 연결 성공 시 호출
    fun onSuccess(purchase: Purchase) // 구매 성공 시 호출 Purchase : 구매정보
    fun onFailure(responseCode: Int) // 구매 실패 시 호출 errorCode : BillingResponseCode
}