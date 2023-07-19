package com.example.domain.entity

data class ProfileUserModel(
    val userId: Int,
    val name: String,
    val profileImageUrl: String,
    val group: String,
    val yelloId: String,
    val yelloCount: Int,
    val friendCount: Int,
    val point: Int = 0
)