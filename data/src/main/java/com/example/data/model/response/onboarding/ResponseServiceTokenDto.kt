package com.example.data.model.response.onboarding

import com.example.domain.entity.AuthTokenModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseServiceTokenDto(
    @SerialName("isResigned")
    val isResigned: Boolean,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
) {

    fun toServiceTokenModel(): AuthTokenModel {
        return AuthTokenModel(isResigned, accessToken, refreshToken)
    }
}
