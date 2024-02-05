package com.example.data.model.request.profile

import com.example.domain.entity.ProfileQuitReasonModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestQuitReasonDto(
    @SerialName("value")
    val value: String,
)

fun ProfileQuitReasonModel.toRequestDto() = RequestQuitReasonDto(value)
