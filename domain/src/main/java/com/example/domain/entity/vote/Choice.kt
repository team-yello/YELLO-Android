package com.example.domain.entity.vote

data class Choice(
    val questionId: Int,
    var friendId: Int? = null,
    var friendName: String? = null,
    var keywordName: String? = null,
)
