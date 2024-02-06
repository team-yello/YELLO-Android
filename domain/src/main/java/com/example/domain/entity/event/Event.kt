package com.example.domain.entity.event

data class Event(
    val isAvailable: Boolean,
    val title: String = "",
    val subTitle: String = "",
    val defaultLottieJson: String = "",
    val openLottieJson: String = "",
    val rewardList: List<Reward>? = null,
) {
    data class Reward(
        val imageUrl: String,
        val name: String,
    )
}
