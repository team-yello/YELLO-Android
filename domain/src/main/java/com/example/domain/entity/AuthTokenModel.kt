package com.example.domain.entity

data class AuthTokenModel(
    val isResigned: Boolean,
    val accessToken: String,
    val refreshToken: String,
)
