package com.example.data.model.request.onboarding

import com.example.domain.entity.RequestServiceTokenModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestServiceTokenDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("social")
    val social: String
) {

}

fun RequestServiceTokenModel.toRequestDto(): RequestServiceTokenDto {
    return RequestServiceTokenDto(accessToken, social)
}