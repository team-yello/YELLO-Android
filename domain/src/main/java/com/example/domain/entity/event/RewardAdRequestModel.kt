package com.example.domain.entity.event

data class RewardAdRequestModel(
    val rewardType: String,
    val randomType: String,
    val uuid: String,
    val rewardNumber: Int
)