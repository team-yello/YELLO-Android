package com.example.domain.entity

data class ProfileUserModel(
    val friendCount: Int,
    val group: String,
    val id: Int,
    val name: String,
    val point: Int,
    val profileImageUrl: String,
    val yelloCount: Int,
    val yelloId: String
)