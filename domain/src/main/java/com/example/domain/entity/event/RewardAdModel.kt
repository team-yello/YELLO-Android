package com.example.domain.entity.event

data class RewardAdModel(
    val rewardTag: String,
    val rewardValue: Int,
    val rewardTitle: String,
    val rewardImage: String
) {
    constructor() : this("",0,"","")
}