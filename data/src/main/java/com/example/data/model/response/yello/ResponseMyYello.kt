package com.example.data.model.response.yello

import com.example.domain.entity.MyYello
import com.example.domain.entity.Yello
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyYello(
    val yelloCount: Int,
    val yello: List<YelloDto>
) {
    fun toTotalYello(): MyYello {
        return MyYello(yelloCount, yello.map { it.toYello() })
    }
}

@Serializable
data class YelloDto(
    val id: Int,
    val sex: String,
    val createdAt: String
) {
    fun toYello(): Yello {
        return Yello(id, sex, createdAt)
    }
}