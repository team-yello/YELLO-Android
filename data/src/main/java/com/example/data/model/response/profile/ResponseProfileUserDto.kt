package com.example.data.model.response.profile

import com.example.domain.entity.ProfileUserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileUserDto(
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
) {
    fun toProfileUserModel(): ProfileUserModel {
        return ProfileUserModel(
            userId, name, profileImageUrl, group, yelloId, yelloCount, friendCount, point
        )
    }
}