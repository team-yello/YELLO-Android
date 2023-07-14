package com.example.domain.entity

data class Choice(
    val questionId: Int,
    var friendId: Int? = null,
    var friendName: String? = null,
    var keywordName: String? = null,
)
