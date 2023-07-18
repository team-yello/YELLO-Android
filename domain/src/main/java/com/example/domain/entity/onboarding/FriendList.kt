package com.example.domain.entity.onboarding

import java.lang.Boolean.TRUE
import kotlin.String

data class FriendList(
    val totalCount: Int,
    val friendList: List<Friend>,
) {
    fun toIdList() = friendList.map { friend -> friend.id }
}

data class Friend(
    val group: String,
    val id: Long,
    val name: String,
    val profileImage: String,
    val groupName: String,
    var isSelected: Boolean = TRUE,
)
