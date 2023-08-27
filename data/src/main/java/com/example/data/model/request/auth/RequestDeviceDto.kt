package com.example.data.model.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class RequestDeviceDto(
    val deviceToken: String
)

fun String.toDeviceToken() : RequestDeviceDto {
    return RequestDeviceDto(this)
}