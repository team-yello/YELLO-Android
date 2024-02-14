package com.example.data.model.response.event

import com.example.domain.entity.event.Event.Reward
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetEventDto(
    @SerialName("tag")
    val tag: String,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("endDate")
    val endDate: String,
    @SerialName("title")
    val title: String,
    @SerialName("subTitle")
    val subTitle: String,
    @SerialName("animationList")
    val animationList: List<String>,
    @SerialName("eventReward")
    val eventReward: RewardDto?,
) {

    @Serializable
    data class RewardDto(
        @SerialName("startTime")
        val startTime: String,
        @SerialName("endTime")
        val endTime: String,
        @SerialName("rewardCount")
        val rewardCount: Int,
        @SerialName("eventRewardItem")
        val eventRewardItem: List<RewardItemDto>,

    ) {
        @Serializable
        data class RewardItemDto(
            @SerialName("tag")
            val tag: String,
            @SerialName("eventRewardTitle")
            val eventRewardTitle: String,
            @SerialName("eventRewardImage")
            val eventRewardImage: String,
            @SerialName("maxRewardValue")
            val maxRewardValue: Int,
            @SerialName("minRewardValue")
            val minRewardValue: Int,
            @SerialName("eventRewardProbability")
            val eventRewardProbability: Int,
            @SerialName("randomTag")
            val randomTag: String,
        ) {
            fun toReward(): Reward = Reward(
                imageUrl = eventRewardImage,
                name = eventRewardTitle,
            )
        }
    }
}
