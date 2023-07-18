package com.example.data.model.response.vote

import com.example.domain.entity.vote.Note.Friend
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetFriendShuffleDto(
    @SerialName("friendId")
    val friendId: Int,
    @SerialName("friendName")
    val friendName: String,
    @SerialName("friendYelloId")
    val friendYelloId: String
) {
    fun toFriend() = Friend(
        id = friendId,
        yelloId = friendYelloId,
        name = friendName,
    )
}
