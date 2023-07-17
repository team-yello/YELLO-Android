package com.example.data.model.response.profile

import com.example.domain.entity.ProfileUserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileUserDto(
    @SerialName("friendCount")
    val friendCount: Int,
    @SerialName("group")
    val group: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("point")
    val point: Int,
    @SerialName("profileImageUrl")
    val profileImageUrl: String,
    @SerialName("yelloCount")
    val yelloCount: Int,
    @SerialName("yelloId")
    val yelloId: String
) {
    fun toProfileUserModel(): ProfileUserModel {
        return ProfileUserModel(
            friendCount, group, id, name, point, profileImageUrl, yelloCount, yelloId
        )
    }
}