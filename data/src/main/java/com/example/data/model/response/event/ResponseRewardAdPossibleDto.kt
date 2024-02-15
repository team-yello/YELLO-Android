package com.example.data.model.response.event

import com.example.data.util.toRemainTime
import com.example.domain.entity.event.RewardAdPossibleModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRewardAdPossibleDto(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("isPossible")
    val isPossible: Boolean
) {
    fun toRewardAdPossibleModel() = RewardAdPossibleModel(
        leftTime = createdAt.toRemainTime(3600),
        isPossible = isPossible
    )
}