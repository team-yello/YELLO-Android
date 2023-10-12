package com.example.data.model.response.pay

import com.example.domain.entity.PaySubsModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePaySubsDto(
    @SerialName("productId")
    val productId: String,
    @SerialName("expiredAt")
    val expiredAt: String
) {
    fun toResponsePaySubsModel(): PaySubsModel {
        return PaySubsModel(
            productId, expiredAt
        )
    }
}