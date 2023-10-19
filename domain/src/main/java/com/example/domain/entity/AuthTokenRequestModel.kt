package com.example.domain.entity

data class AuthTokenRequestModel(
    val accessToken: String,
    val social: String,
    val deviceToken: String
)
