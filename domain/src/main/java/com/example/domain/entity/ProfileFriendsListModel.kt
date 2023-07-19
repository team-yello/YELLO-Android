package com.example.domain.entity

data class ProfileFriendsListModel(
    val totalCount: Int,
    val friends: List<ProfileFriendModel>
) {
    data class ProfileFriendModel(
        val userId: Int,
        val name: String,
        val profileImageUrl: String,
        val group: String,
        val yelloId: String,
        val yelloCount: Int,
        val friendCount: Int
    )
}