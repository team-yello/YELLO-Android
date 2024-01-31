package com.example.domain.entity

data class LookListModel(
    val totalCount: Int,
    val friendVotes: List<LookModel>
) {
    data class LookModel(
        val id: Long,
        val receiverName: String,
        val senderGender: String,
        val receiverProfileImage: String,
        val vote: LookVoteModel,
        val isHintUsed: Boolean,
        val createdAt: String,
        val isUserSenderVote: Boolean
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