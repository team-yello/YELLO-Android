package com.example.domain.entity

data class RequestServiceTokenModel(
    val accessToken: String,
    val social: String,
    val deviceToken: String
)
