package com.example.domain.entity

data class PayRequestModel(
    val orderId: String?,
    val packageName: String,
    val productId: String,
    val purchaseTime: Long,
    val purchaseState: Int,
    val purchaseToken: String,
    val quantity: Int,
    val autoRenewing: Boolean = true,
    val acknowledged: Boolean,
)