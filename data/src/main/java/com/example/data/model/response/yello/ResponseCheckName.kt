package com.example.data.model.response.yello

import com.example.domain.entity.CheckName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCheckName(
    val nameIndex: Int,
    val name: String
) {
    fun toCheckName(): CheckName {
        return CheckName(nameIndex, name)
    }
}
