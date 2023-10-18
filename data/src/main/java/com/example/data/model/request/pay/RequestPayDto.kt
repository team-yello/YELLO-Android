package com.example.data.model.request.pay

import com.android.billingclient.api.Purchase
import com.example.domain.entity.PayRequestModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPayDto(
    @SerialName("orderId")
    val orderId: String?,
    @SerialName("packageName")
    val packageName: String,
    @SerialName("productId")
    val productId: String,
    @SerialName("purchaseTime")
    val purchaseTime: Long,
    @SerialName("purchaseState")
    val purchaseState: Int,
    @SerialName("purchaseToken")
    val purchaseToken: String,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("autoRenewing")
    val autoRenewing: Boolean = true,
    @SerialName("acknowledged")
    val acknowledged: Boolean,
)

fun PayRequestModel.toRequestDto() = RequestPayDto(
    orderId = orderId,
    packageName = packageName,
    productId = productId,
    purchaseTime = purchaseTime,
    purchaseState = purchaseState,
    purchaseToken = purchaseToken,
    quantity = quantity,
    autoRenewing = autoRenewing,
    acknowledged = acknowledged
)

fun Purchase.toRequestPayModel() = PayRequestModel(
    orderId = orderId,
    packageName = packageName,
    productId = products[0],
    purchaseTime = purchaseTime,
    purchaseState = purchaseState,
    purchaseToken = purchaseToken,
    quantity = quantity,
    autoRenewing = isAutoRenewing,
    acknowledged = isAcknowledged
)