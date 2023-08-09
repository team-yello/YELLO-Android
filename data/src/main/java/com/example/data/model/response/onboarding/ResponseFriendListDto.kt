package com.example.data.model.response.onboarding

import com.example.domain.entity.onboarding.AddFriendListModel.FriendModel
import com.example.domain.entity.onboarding.AddFriendListModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseFriendListDto(
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
        fun toFriendModel(): FriendModel {
            return FriendModel(group, id, name, profileImage, groupName)
        }
    }

    fun toFriendListModel(): AddFriendListModel {
        return AddFriendListModel(totalCount, friendList.map { it.toFriendModel() })
    }
}
