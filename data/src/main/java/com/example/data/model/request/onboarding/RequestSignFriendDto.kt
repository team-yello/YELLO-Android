package com.example.data.model.request.onboarding

import com.example.domain.entity.onboarding.FriendGroup
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestSignFriendDto(
    @SerialName("friendKaKaoId")
    val friendKaKaoId: List<String>,
    @SerialName("groupId")
    val groupId: Long,
)

fun FriendGroup.toRequestSignFriendDto() = RequestSignFriendDto(
    friendKaKaoId = friendIdList,
    groupId = groupId,
)
