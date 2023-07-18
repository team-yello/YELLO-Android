package com.example.domain.entity

data class ProfileFriendModel(
    val id: Int,
    val name: String,
    val yelloId: String,
    val school: String,
    val thumbnail: String?,
    val totalMsg: Int,
    val totalFriends: Int,
)
