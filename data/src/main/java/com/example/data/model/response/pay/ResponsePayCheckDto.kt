package com.example.data.model.response.pay

import com.example.domain.entity.ResponsePayCheckModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePayCheckDto(
    @SerialName("subscribe")
    val subscribe: String,
    @SerialName("isSubscribeNeeded")
    val isSubscribeNeeded: Boolean
) {
    fun toResponsePayCheckModel(): ResponsePayCheckModel {
        return ResponsePayCheckModel(
            subscribe, isSubscribeNeeded
        )
    }
}