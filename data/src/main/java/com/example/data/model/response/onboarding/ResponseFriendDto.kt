package com.example.data.model.response.onboarding

import com.example.domain.entity.onboarding.Friend
import com.example.domain.entity.onboarding.FriendList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseFriendDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("friendList")
    val friendList: List<FriendListDto>,
) {
    @Serializable
    data class FriendListDto(
        @SerialName("group")
        val group: String,
        @SerialName("id")
        val id: Long,
        @SerialName("name")
        val name: String,
        @SerialName("profileImage")
        val profileImage: String,
        @SerialName("groupName")
        val groupName: String,

    ) {
        fun toFriendList(): Friend {
            return Friend(group, id, name, profileImage, groupName)
        }
    }

    fun toMyFriend(): FriendList {
        return FriendList(totalCount, friendList.map { it.toFriendList() })
    }
}
