package com.example.data.model.response.profile

import com.example.domain.entity.ProfileUserModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileUserDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("name")
    val name: String,
    @SerialName("yelloId")
    val yelloId: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("email")
    val email: String,
    @SerialName("profileImageUrl")
    val profileImageUrl: String,
    @SerialName("social")
    val social: String,
    @SerialName("uuid")
    val uuid: String,
    @SerialName("deviceToken")
    val deviceToken: String,
    @SerialName("group")
    val group: String,
    @SerialName("groupType")
    val groupType: String,
    @SerialName("groupName")
    val groupName: String,
    @SerialName("subGroupName")
    val subGroupName: String,
    @SerialName("groupAdmissionYear")
    val groupAdmissionYear: Int,
    @SerialName("recommendCount")
    val recommendCount: Long,
    @SerialName("ticketCount")
    val ticketCount: Int,
    @SerialName("point")
    val point: Int,
    @SerialName("subscribe")
    val subscribe: String,
    @SerialName("yelloCount")
    val yelloCount: Int,
    @SerialName("friendCount")
    val friendCount: Int,

) {
    fun toProfileUserModel(): ProfileUserModel {
        return ProfileUserModel(
            userId = userId,
            name = name,
            profileImageUrl = profileImageUrl,
            group = group,
            yelloId = yelloId,
            yelloCount = yelloCount,
            friendCount = friendCount,
            point = point,
        )
    }
}
