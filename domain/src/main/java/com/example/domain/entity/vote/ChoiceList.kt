package com.example.domain.entity.vote

data class ChoiceList(
    val choiceList: List<Choice>,
    val totalPoint: Int = 0,
)
