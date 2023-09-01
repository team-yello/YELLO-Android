package com.example.data.model.response.yello

import com.example.domain.entity.MyYello
import com.example.domain.entity.Vote
import com.example.domain.entity.Yello
import com.example.domain.enum.GenderEnum
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyYello(
    val totalCount: Int,
    val ticketCount: Int,
    val openCount: Int = 0,
    val openKeywordCount: Int = 0,
    val openNameCount: Int = 0,
    val openFullNameCount: Int = 0,
    val votes: List<YelloDto>,
) {
    fun toTotalYello(): MyYello {
        return MyYello(
            totalCount,
            ticketCount,
            openCount,
            openKeywordCount,
            openNameCount,
            openFullNameCount,
            votes.map { it.toYello() })
    }
}

@Serializable
data class YelloDto(
    val id: Long,
    val senderGender: String,
    val nameHint: Int,
    val senderName: String,
    val vote: VoteDto,
    val isHintUsed: Boolean,
    val isRead: Boolean,
    val createdAt: String,
) {
    fun toYello(): Yello {
        val genderEnum = if (senderGender.contains("FE")) GenderEnum.W else GenderEnum.M
        return Yello(
            id,
            genderEnum,
            nameHint,
            senderName,
            vote.toVote(),
            isHintUsed,
            isRead,
            createdAt,
        )
    }
}

@Serializable
data class VoteDto(
    val nameHead: String?,
    val nameFoot: String?,
    val keywordHead: String?,
    val keyword: String,
    val keywordFoot: String?
) {
    fun toVote(): Vote {
        return Vote(nameHead ?: "", nameFoot ?: "", keywordHead ?: "", keyword, keywordFoot ?: "")
    }
}
