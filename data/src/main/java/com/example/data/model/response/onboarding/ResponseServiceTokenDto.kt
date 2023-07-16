package com.example.data.model.response.onboarding

import com.example.domain.entity.ServiceTokenModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

    @Serializable
    data class ResponseServiceToken(
        @SerialName("accessToken")
        val accessToken: String,
        @SerialName("refreshToken")
        val refreshToken: String
    ) {

    fun toServiceTokenModel(): ServiceTokenModel {
        return ServiceTokenModel(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}