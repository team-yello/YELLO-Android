package com.example.domain.entity

data class ProfileFriendsModel(
    val totalCount : Int,
    val friends: List<ProfileUserModel>
)