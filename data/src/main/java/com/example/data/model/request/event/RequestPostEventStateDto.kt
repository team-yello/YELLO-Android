package com.example.data.model.request.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostEventStateDto(
    @SerialName("tag")
    val tag: String,
)
