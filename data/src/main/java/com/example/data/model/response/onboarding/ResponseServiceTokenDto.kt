package com.example.data.model.response.onboarding

import com.example.domain.entity.ServiceTokenModel
import kotlinx.serialization.Serializable

@Serializable
data class ResponseServiceTokenDto(
    val status: Int,
    val message: String,
    val data: ServiceToken
) {
    @Serializable
    data class ServiceToken(
        val accessToken: String,
        val refreshToken: String
    )

    fun ResponseServiceTokenDto.toServiceTokenModel(): ServiceTokenModel {
        return ServiceTokenModel(
            accessToken = data.accessToken,
            refreshToken = data.refreshToken
        )
    }
}