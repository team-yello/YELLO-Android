package com.example.data.model.response.vote

import com.example.domain.entity.vote.VoteState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetVoteAvailableDto(
    @SerialName("isStart")
    val isStart: Boolean,
    @SerialName("point")
    val point: Int,
    @SerialName("createdAt")
    val createdAt: String,
) {
    fun toVoteState() = VoteState(
        isStart = isStart,
        point = point,
        leftTime = 1000, // TODO: 남은 시간 변환 함수 구현
    )
}
