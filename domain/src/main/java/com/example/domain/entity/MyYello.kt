package com.example.domain.entity

import com.example.domain.enum.Gender

data class MyYello(
    val totalCount: Int,
    val ticketCount: Int,
    val openCount: Int = 0,
    val openKeywordCount: Int = 0,
    val openNameCount: Int = 0,
    val openFullNameCount: Int = 0,
    val yello: List<Yello>,
)

data class Yello(
    val id: Long,
    val gender: Gender,
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
