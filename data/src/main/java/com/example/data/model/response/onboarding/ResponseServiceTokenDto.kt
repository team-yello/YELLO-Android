package com.example.data.model.response.onboarding

import com.example.domain.entity.ServiceTokenModel
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

    fun toServiceTokenModel(): ServiceTokenModel {
        return ServiceTokenModel(isResigned, accessToken, refreshToken)
    }
}
