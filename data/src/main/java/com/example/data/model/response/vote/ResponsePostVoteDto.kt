package com.example.data.model.response.vote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePostVoteDto(
    @SerialName("point")
    val point: Int,
)
