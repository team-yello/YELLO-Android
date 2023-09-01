package com.example.domain.entity.vote

data class StoredVote(
    val currentIndex: Int,
    val choiceList: MutableList<Choice>,
    val totalPoint: Int,
    val questionList: List<Note>,
)
