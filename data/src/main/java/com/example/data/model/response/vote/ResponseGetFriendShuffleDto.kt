package com.example.data.model.response.vote

import com.example.domain.entity.Note.Friend
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetFriendShuffleDto(
    @SerialName("friendId")
    val friendId: Int,
    @SerialName("friendName")
    val friendName: String,
) {
    fun toFriend() = Friend(
        id = friendId,
        yelloId = "dummy", // TODO: 명세서 수정 후 yello id 넣어주기
        name = friendName,
    )
}
