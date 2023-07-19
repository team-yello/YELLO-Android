package com.example.domain.entity

data class ProfileFriendsListModel(
    val totalCount: Int,
    val friends: List<ProfileUserModel>
)