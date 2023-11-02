package com.example.data.model.request.onboarding

import com.example.domain.entity.AuthTokenRequestModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestServiceTokenDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("social")
    val social: String,
    @SerialName("deviceToken")
    val deviceToken: String
)

fun AuthTokenRequestModel.toRequestDto(): RequestServiceTokenDto {
    return RequestServiceTokenDto(accessToken, social, deviceToken)
}
