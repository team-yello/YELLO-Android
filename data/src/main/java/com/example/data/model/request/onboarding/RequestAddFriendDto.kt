package com.example.data.model.request.onboarding

import com.example.domain.entity.onboarding.RequestAddFriendModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestAddFriendDto(
    @SerialName("friendKakaoId")
    val friendKakaoId: List<String>,
    @SerialName("groupId")
    val groupId: Long,
)

fun RequestAddFriendModel.toRequestDto() = RequestAddFriendDto(
    friendKakaoId = friendKakaoId,
    groupId = groupId,
)