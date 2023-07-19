package com.example.data.model.response

import com.example.domain.entity.Response
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto(
    val status: Int,
    val message: String
) {
    fun toResponse(): Response {
        return Response(status, message)
    }
}