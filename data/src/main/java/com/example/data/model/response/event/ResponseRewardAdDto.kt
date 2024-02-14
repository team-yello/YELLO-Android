package com.example.data.model.response.event

import com.example.domain.entity.event.RewardAdModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRewardAdDto(
    @SerialName("rewardTag")
    val rewardTag: String,
    @SerialName("rewardValue")
    val rewardValue: Int,
    @SerialName("rewardTitle")
    val rewardTitle: String,
    @SerialName("rewardImage")
    val rewardImage: String
) {
    fun toRewardAdModel(): RewardAdModel = RewardAdModel(
        rewardTag, rewardValue, rewardTitle, rewardImage
    )
}