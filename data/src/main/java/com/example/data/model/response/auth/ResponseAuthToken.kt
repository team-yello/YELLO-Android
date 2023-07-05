package com.example.data.model.response.auth

data class ResponseAuthToken(
    val accessToken: String,
    val refreshToken: String,
)
