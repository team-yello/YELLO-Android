package com.example.data.model.response.pay

import com.example.domain.entity.PayUserSubsInfoModel
import com.example.domain.enum.toSubscribeType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserSubsInfoDto(
    @SerialName("id")
    val id: Long,
    @SerialName("subscribe")
    val subscribe: String,
    @SerialName("expiredDate")
    val expiredDate: String,
) {
    fun toResponseUserSubsInfoModel(): PayUserSubsInfoModel {
        return PayUserSubsInfoModel(
            id = id,
            subscribe = subscribe.toSubscribeType(),
            expiredDate = expiredDate,
        )
    }
}
