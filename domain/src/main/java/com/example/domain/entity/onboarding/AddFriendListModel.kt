package com.example.domain.entity.onboarding

import java.lang.Boolean.TRUE

data class AddFriendListModel(
    val totalCount: Int,
    val friendList: List<FriendModel>,
) {
    data class FriendModel(
        val group: String,
        val id: Long,
        val name: String,
        val profileImage: String,
        val groupName: String,
        var isSelected: Boolean = TRUE,
    )
}
