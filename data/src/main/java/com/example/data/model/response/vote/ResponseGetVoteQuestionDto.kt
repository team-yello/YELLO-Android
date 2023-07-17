package com.example.data.model.response.vote

import com.example.domain.entity.vote.Note
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetVoteQuestionDto(
    @SerialName("question")
    val question: QuestionDto,
    @SerialName("friendList")
    val friendList: List<FriendDto>,
    @SerialName("keywordList")
    val keywordList: List<String>,
    @SerialName("questionPoint")
    val questionPoint: Int,
) {
    @Serializable
    data class QuestionDto(
        @SerialName("questionId")
        val questionId: Int,
        @SerialName("nameHead")
        val nameHead: String?,
        @SerialName("nameFoot")
        val nameFoot: String?,
        @SerialName("keywordHead")
        val keywordHead: String?,
        @SerialName("keywordFoot")
        val keywordFoot: String?,
    )

    @Serializable
    data class FriendDto(
        @SerialName("id")
        val id: Int,
        @SerialName("yelloId")
        val yelloId: String,
        @SerialName("name")
        val name: String,
    ) {
        fun toFriend() = Note.Friend(
            id = id,
            yelloId = yelloId,
            name = name,
        )
    }

    fun toNote() = Note(
        questionId = question.questionId,
        nameHead = question.nameHead ?: "",
        nameFoot = question.nameFoot ?: "",
        keywordHead = question.keywordHead ?: "",
        keywordFoot = question.keywordFoot ?: "",
        friendList = friendList.map { friend -> friend.toFriend() },
        keywordList = keywordList,
        point = questionPoint,
    )
}
