package com.example.data.model.request.profile

import com.example.domain.entity.ProfileModRequestModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileModRequestDto(
    @SerialName("name")
    val name: String,
    @SerialName("yelloId")
    val yelloId: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("profileImageUrl")
    val profileImageUrl: String,
    @SerialName("groupId")
    val groupId: Long,
    @SerialName("groupAdmissionYear")
    val groupAdmissionYear: Long
)

fun ProfileModRequestModel.toRequestDto() =
    ProfileModRequestDto(name, yelloId, gender, profileImageUrl, groupId, groupAdmissionYear)
