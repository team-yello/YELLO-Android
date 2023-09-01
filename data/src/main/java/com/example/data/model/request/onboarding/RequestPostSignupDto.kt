package com.example.data.model.request.onboarding

import com.example.domain.entity.onboarding.SignupInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostSignupDto(
    @SerialName("social")
    val social: String,
    @SerialName("uuid")
    val uuid: String,
    @SerialName("deviceToken")
    val deviceToken: String,
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
    val recommendId: String? = null,
)

fun SignupInfo.toRequestPostSignupDto() = RequestPostSignupDto(
    social = "KAKAO",
    uuid = kakaoId,
    email = email,
    profileImage = profileImg,
    groupId = groupId,
    groupAdmissionYear = studentId,
    name = name,
    yelloId = yelloId,
    gender = gender,
    friends = friendList,
    recommendId = recommendId,
    deviceToken = deviceToken
)
