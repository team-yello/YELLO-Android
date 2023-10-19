package com.example.data.model.response.pay

import com.example.domain.entity.PaySubsNeededModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSubsNeededDto(
    @SerialName("subscribe")
    val subscribe: String,
    @SerialName("isSubscribeNeeded")
    val isSubscribeNeeded: Boolean
) {
    fun toResponseSubsNeededModel(): PaySubsNeededModel {
        return PaySubsNeededModel(
            subscribe, isSubscribeNeeded
        )
    }
}