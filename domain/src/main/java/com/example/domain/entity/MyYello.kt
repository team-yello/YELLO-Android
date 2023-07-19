package com.example.domain.entity

import com.example.domain.enum.GenderEnum

data class MyYello(
    val totalCount: Int,
    val yello: List<Yello>,
)

data class Yello(
    val id: Long,
    val gender: GenderEnum,
    var nameHint: Int,
    val senderName: String,
    val vote: Vote,
    var isHintUsed: Boolean,
    var isRead: Boolean,
    val createdAt: String
)

data class Vote(
    val nameHead: String,
    val nameFoot: String,
    val keywordHead: String,
    val keyword: String,
    val keywordFoot: String,
)
