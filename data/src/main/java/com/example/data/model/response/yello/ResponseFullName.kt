package com.example.data.model.response.yello

import com.example.domain.entity.FullName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseFullName(
    val name: String
) {
    fun toFullName(): FullName {
        return FullName(name)
    }
}