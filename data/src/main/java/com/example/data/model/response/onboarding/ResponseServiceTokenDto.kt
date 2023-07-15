package com.example.data.model.response.onboarding

import com.example.domain.entity.ServiceTokenModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseServiceTokenDto(
    @SerialName("status")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: ServiceToken
) {
    @Serializable
    data class ServiceToken(
        @SerialName("accessToken")
        val accessToken: String,
        @SerialName("refreshToken")
        val refreshToken: String
    )

    fun toServiceTokenModel(): ServiceTokenModel {
        return ServiceTokenModel(
            accessToken = data.accessToken,
            refreshToken = data.refreshToken
        )
    }
}