package com.example.data.model.response.yello

import com.example.domain.entity.YelloDetail
import com.example.domain.entity.YelloDetailVote
import kotlinx.serialization.Serializable

@Serializable
data class ResponseYelloDetail(
    val nameHint: Int,
    val isAnswerRevealed: Boolean,
    val senderName: String,
    val vote: YelloDetailVoteDto
) {
    fun toYelloDetail(): YelloDetail {
        return YelloDetail(nameHint, isAnswerRevealed, senderName, vote.toYelloDetailVote())
    }
}

@Serializable
data class YelloDetailVoteDto(
    val head: String,
    val answer: String,
    val foot: String
) {
    fun toYelloDetailVote(): YelloDetailVote {
        return YelloDetailVote(head, answer, foot)
    }
}