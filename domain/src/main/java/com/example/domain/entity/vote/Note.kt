package com.example.domain.entity.vote

data class Note(
    val questionId: Int,
    val nameHead: String,
    val nameFoot: String,
    val keywordHead: String,
    val keywordFoot: String,
    var friendList: List<Friend>,
    val keywordList: List<String>,
) {
    data class Friend(
        val id: Int,
        val yelloId: String,
        val name: String,
    )
}
