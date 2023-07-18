package com.example.domain.entity

data class YelloDetail(
    val currentPoint: Int,
    val colorIndex: Int,
    val nameHint: Int,
    val isAnswerRevealed: Boolean,
    val senderGender: String,
    val senderName: String,
    val vote: Vote
)