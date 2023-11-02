package com.example.data.model.response.pay

import com.example.domain.entity.PayInfoModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ResponsePurchaseInfoDto(
    @SerialName("subscribeState")
    val subscribeState: String,
    @SerialName("isSubscribe")
    val isSubscribe: Boolean,
    @SerialName("ticketCount")
    val ticketCount: Int
) {
    fun toResponsePurchaseInfoModel(): PayInfoModel {
        return PayInfoModel(
            subscribeState, isSubscribe, ticketCount
        )
    }
}