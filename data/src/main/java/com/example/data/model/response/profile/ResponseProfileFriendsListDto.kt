package com.example.data.model.response.profile

import com.example.domain.entity.ProfileFriendsListModel
import com.example.domain.entity.ProfileUserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileFriendsListDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("friends")
    val friends: List<ResponseProfileFriendModel>,
) {
    @Serializable
    data class ResponseProfileFriendModel(
        @SerialName("userId")
        val userId: Long,
        @SerialName("name")
        val name: String,
        @SerialName("profileImageUrl")
        val profileImageUrl: String,
        @SerialName("group")
        val group: String,
        @SerialName("yelloId")
        val yelloId: String,
        @SerialName("yelloCount")
        val yelloCount: Int,
        @SerialName("friendCount")
        val friendCount: Int,

    )

    fun toProfileFriendsListModel(): ProfileFriendsListModel {
        return ProfileFriendsListModel(
            totalCount,
            friends.map {
                ProfileUserModel(
                    userId = it.userId,
                    name = it.name,
                    profileImageUrl = it.profileImageUrl,
                    group = it.group,
                    yelloId = it.yelloId,
                    gender = "",
                    email = "",
                    yelloCount = it.yelloCount,
                    friendCount = it.friendCount,
                    point = 0,
                    groupId = 0,
                    groupType = "",
                    groupName = "",
                    subGroupName = "",
                    groupAdmissionYear = 0,
                    subscribe = "",
                    ticketCount = 0
                )
            },
        )
    }
}
