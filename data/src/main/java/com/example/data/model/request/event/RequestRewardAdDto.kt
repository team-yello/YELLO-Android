package com.example.data.model.request.event

import com.example.domain.entity.event.RewardAdRequestModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestRewardAdDto(
    @SerialName("rewardType")
    val rewardType: String,
    @SerialName("randomType")
    val randomType: String,
    @SerialName("uuid")
    val uuid: String,
    @SerialName("rewardNumber")
    val rewardNumber: Int
)

fun RewardAdRequestModel.toRequestDto() = RequestRewardAdDto(
    rewardType, randomType, uuid, rewardNumber
)