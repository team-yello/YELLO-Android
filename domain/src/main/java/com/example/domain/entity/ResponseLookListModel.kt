package com.example.domain.entity

data class ResponseLookListModel(
    val totalCount: Int,
    val friendVotes: List<LookModel>
) {
    data class LookModel(
        val id: Int,
        val receiverName: String,
        val senderGender: String,
        val vote: LookVoteModel,
        val isHintUsed: Boolean,
        val createdAt: String
    ) {
        data class LookVoteModel(
            val nameHead: String?,
            val nameFoot: String?,
            val keywordHead: String?,
            val keyword: String?,
            val keywordFoot: String?
        )
    }
}