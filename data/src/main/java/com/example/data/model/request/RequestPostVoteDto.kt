package com.example.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostVoteDto(
    @SerialName("voteList")
    val voteList: List<VoteDto>,
    @SerialName("totalPoint")
    val totalPoint: Int,
) {
    @Serializable
    data class VoteDto(
        @SerialName("friendId")
        val friendId: Int,
        @SerialName("questionId")
        val questionId: Int,
        @SerialName("keywordName")
        val keywordName: String,
        @SerialName("colorIndex")
        val colorIndex: Int,
    )
}
