package com.example.data.model.response.recommend

import com.example.domain.entity.RecommendModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRecommendDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("group")
    val group: String,
    @SerialName("profileImage")
    val profileImage: String?
) {
    fun toRecommendModel(): RecommendModel {
        return RecommendModel(
            id, name, group, profileImage
        )
    }
}