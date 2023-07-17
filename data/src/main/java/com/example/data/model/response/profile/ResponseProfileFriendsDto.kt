package com.example.data.model.response.profile

import com.example.domain.entity.ProfileFriendsModel
import com.example.domain.entity.ProfileUserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileFriendsDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("friends")
    val friends: List<ResponseProfileUserModel>
) {
    @Serializable
    data class ResponseProfileUserModel(
        @SerialName("userId")
        val userId: Int,
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
        @SerialName("point")
        val point: Int
    )

    fun toProfileFriendsModel(): ProfileFriendsModel {
        return ProfileFriendsModel(
            totalCount, friends.map {
                ProfileUserModel(
                    it.userId, it.name, it.profileImageUrl, it.group, it.yelloId, it.yelloCount, it.friendCount, it.point
                )
            }
        )
    }
}