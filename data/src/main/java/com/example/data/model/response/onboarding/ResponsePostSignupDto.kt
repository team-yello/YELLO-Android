package com.example.data.model.response.onboarding

import com.example.domain.entity.onboarding.UserInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePostSignupDto(
    @SerialName("yelloId")
    val yelloId: String,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
) {
    fun toUserInfo() = UserInfo(
        yelloId = yelloId,
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
}
