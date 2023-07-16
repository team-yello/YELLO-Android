package com.example.data.model.response.vote

import com.example.domain.entity.vote.Point
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePostVoteDto(
    @SerialName("point")
    val point: Int,
) {
    fun toPoint() = Point(
        point = point,
    )
}
