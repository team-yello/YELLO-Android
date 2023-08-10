package com.example.data.model.response.look

import com.example.domain.entity.LookListModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseLookListDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("friendVotes")
    val friendVotes: List<LookDto>
) {
    @Serializable
    data class LookDto(
        @SerialName("id")
        val id: Int,
        @SerialName("receiveName")
        val receiveName: String,
        @SerialName("senderGender")
        val senderGender: String,
        @SerialName("vote")
        val vote: LookVoteDto,
        @SerialName("isHintUsed")
        val isHintUsed: Boolean,
        @SerialName("createdAt")
        val createdAt: String
    ) {
        @Serializable
        data class LookVoteDto(
            @SerialName("nameHead")
            val nameHead: String?,
            @SerialName("nameFoot")
            val nameFoot: String?,
            @SerialName("keywordHead")
            val keywordHead: String?,
            @SerialName("keyword")
            val keyword: String?,
            @SerialName("keywordFoot")
            val keywordFoot: String?
        )
    }

    fun toLookListModel(): LookListModel {
        return LookListModel(
            totalCount, friendVotes.map {
                LookListModel.LookModel(
                    it.id,
                    it.receiveName,
                    it.senderGender,
                    LookListModel.LookModel.LookVoteModel(
                        it.vote.nameHead,
                        it.vote.nameFoot,
                        it.vote.keywordHead,
                        it.vote.keyword,
                        it.vote.keywordFoot
                    ),
                    it.isHintUsed,
                    it.createdAt
                )
            }
        )
    }
}
