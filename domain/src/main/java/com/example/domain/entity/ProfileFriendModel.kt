package com.example.domain.entity

//TODO: 지우기 !!
data class ProfileFriendModel(
    val id: Int,
    val name: String,
    val yelloId: String,
    val school: String,
    val thumbnail: String?,
    val totalMsg: Int,
    val totalFriends: Int
)