package com.example.data.model.response.yello

import com.example.domain.entity.YelloDetail
import kotlinx.serialization.Serializable

@Serializable
data class ResponseYelloDetail(
    val currentPoint: Int,
    val colorIndex: Int,
    val nameHint: Int,
    val isAnswerRevealed: Boolean,
    val senderGender: String,
    val senderName: String,
    val vote: VoteDto
) {
    fun toYelloDetail(): YelloDetail {
        return YelloDetail(
            currentPoint,
            colorIndex,
            nameHint,
            isAnswerRevealed,
            senderGender,
            senderName,
            vote.toVote()
        )
    }
}