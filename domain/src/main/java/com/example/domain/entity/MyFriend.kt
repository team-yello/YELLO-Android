package com.example.domain.entity

data class MyFriend(
    val profile: Int,
    val name: String,
    val department: String,
    var isSelcted: Boolean = true,
)
