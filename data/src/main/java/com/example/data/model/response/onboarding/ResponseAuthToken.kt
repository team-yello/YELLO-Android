package com.example.data.model.response.onboarding

import kotlinx.serialization.Serializable

@Serializable
data class ResponseAuthToken(
    val accessToken: String,
    val refreshToken: String,
)
