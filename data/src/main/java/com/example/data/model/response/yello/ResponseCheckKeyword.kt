package com.example.data.model.response.yello

import com.example.domain.entity.CheckKeyword
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCheckKeyword(
    val answer: String,
) {
    fun toCheckKeyword(): CheckKeyword {
        return CheckKeyword(answer)
    }
}
