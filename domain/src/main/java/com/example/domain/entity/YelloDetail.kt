package com.example.domain.entity

data class YelloDetail(
    val nameHint: Int,
    val isAnswerRevealed: Boolean,
    val senderName: String,
    val vote: YelloDetailVote
)

data class YelloDetailVote(
    val head: String,
    val answer: String,
    val foot: String
)
