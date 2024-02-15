package com.example.data.model.response.vote

import com.example.data.util.toRemainTime
import com.example.domain.entity.vote.VoteState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetVoteAvailableDto(
    @SerialName("isPossible")
    val isStart: Boolean,
    @SerialName("point")
    val point: Int,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("friendStatus")
    val friendStatus: Int,
) {
    fun toVoteState() = VoteState(
        isStart = isStart,
        point = point,
        leftTime = createdAt.toRemainTime(2400),
        hasFourFriends = friendStatus == STATUS_MORE_THAN_FOUR_FRIENDS,
    )

    companion object {
        private const val STATUS_MORE_THAN_FOUR_FRIENDS = 1
    }
}
