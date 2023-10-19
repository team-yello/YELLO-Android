package com.example.data.model.response.pay

import com.example.domain.entity.PayInAppModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePayInAppDto(
    @SerialName("productId")
    val productId: String,
    @SerialName("ticketCount")
    val ticketCount: Int
) {
    fun toResponsePayInAppModel(): PayInAppModel {
        return PayInAppModel(
            productId, ticketCount
        )
    }
}