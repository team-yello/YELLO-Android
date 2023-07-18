package com.example.data.model.response.recommend

import com.example.domain.entity.RecommendAddModel
import com.example.domain.entity.RecommendModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRecommendAddDto(
    @SerialName("status")
    val status: Int,
    @SerialName("message")
    val message: String
) {
    fun toRecommendAddModel(): RecommendAddModel {
        return RecommendAddModel(
            status, message
        )
    }
}