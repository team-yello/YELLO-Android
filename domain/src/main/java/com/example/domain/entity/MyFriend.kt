package com.example.domain.entity

data class MyFriend(
    val totalCount: Int,
    val FriendList: List<FriendList>,
)
data class FriendList(
    val group: String,
    val id: Long,
    val name: String,
    val profileImage: String,
    val groupName: String,
)
