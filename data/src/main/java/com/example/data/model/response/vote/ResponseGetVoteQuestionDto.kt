package com.example.data.model.response.vote

import com.example.domain.entity.vote.Note

data class ResponseGetVoteQuestionDto(
    val question: QuestionDto,
    val friendList: List<FriendDto>,
    val keywordList: List<String>,
    val questionPoint: Int,
) {
    data class QuestionDto(
        val questionId: Int,
        val nameHead: String,
        val nameFoot: String,
        val keywordHead: String,
        val keywordFoot: String,
    )

    data class FriendDto(
        val id: Int,
        val yelloId: String,
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
        nameHead = question.nameHead,
        nameFoot = question.nameFoot,
        keywordHead = question.keywordHead,
        keywordFoot = question.keywordFoot,
        friendList = friendList.map { friend -> friend.toFriend() },
        keywordList = keywordList,
    )
}
