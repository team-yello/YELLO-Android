package com.example.data.model.response.event

import com.example.domain.entity.event.EventResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePostEventDto(
    @SerialName("rewardTag")
    val rewardTag: String,
    @SerialName("rewardValue")
    val rewardValue: Int,
    @SerialName("rewardTitle")
    val rewardTitle: String,
    @SerialName("rewardImage")
    val rewardImage: String,
) {
    fun toEventResult() = EventResult(
        title = rewardTitle,
        imageUrl = rewardImage,
    )
}
