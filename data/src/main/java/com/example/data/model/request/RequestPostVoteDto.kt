package com.example.data.model.request

import com.example.domain.entity.vote.Choice
import com.example.domain.entity.vote.ChoiceList
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

private fun Choice.toVoteDto() = RequestPostVoteDto.VoteDto(
    questionId = questionId,
    friendId = requireNotNull(friendId),
    keywordName = requireNotNull(keywordName),
    colorIndex = backgroundIndex,
)

fun ChoiceList.toRequestPostVoteDto() = RequestPostVoteDto(
    voteList = choiceList.map { choice -> choice.toVoteDto() },
    totalPoint = totalPoint,
)
