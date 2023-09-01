package com.example.domain.entity

data class ServiceTokenModel(
    val isResigned: Boolean,
    val accessToken: String,
    val refreshToken: String,
)
