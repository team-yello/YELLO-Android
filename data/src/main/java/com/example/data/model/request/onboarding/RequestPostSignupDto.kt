package com.example.data.model.request.onboarding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostSignupDto(
    @SerialName("social")
    val social: String,
    @SerialName("uuid")
    val uuid: Int,
    @SerialName("email")
    val email: String,
    @SerialName("profileImage")
    val profileImage: String,
    @SerialName("groupId")
    val groupId: Long,
    @SerialName("groupAdmissionYear")
    val groupAdmissionYear: Int,
    @SerialName("name")
    val name: String,
    @SerialName("yelloId")
    val yelloId: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("friends")
    val friends: List<Long>,
    @SerialName("recommendId")
    val recommendId: String,
)
