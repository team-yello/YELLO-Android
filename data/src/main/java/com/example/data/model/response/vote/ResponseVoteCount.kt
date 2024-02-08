package com.example.data.model.response.vote

import com.example.domain.entity.vote.VoteCount
import kotlinx.serialization.Serializable

@Serializable
data class ResponseVoteCountDto(
    val totalCount: Int,
) {
    fun toVoteCount(): VoteCount {
        return VoteCount(totalCount)
    }
}
