package com.example.data.model.response.recommend

import com.example.domain.entity.RecommendModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRecommendDto(
    @SerialName("id")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("data")
    val data: List<ResponseRecommendFriend>
) {
    @Serializable
    data class ResponseRecommendFriend(
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("group")
        val group: String,
        @SerialName("profileImage")
        val profileImage: String?
    )

    fun toRecommendModel(): RecommendModel {
        return RecommendModel(
            status, message, totalCount, data.map {
                RecommendModel.RecommendFriend(
                    it.id,
                    it.name,
                    it.group,
                    it.profileImage
                )
            }
        )
    }
}