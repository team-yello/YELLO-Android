package com.example.data.model.response.recommend

import com.example.domain.entity.RecommendListModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRecommendDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("friends")
    val friends: List<ResponseRecommendFriend>
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

    fun toRecommendModel(): RecommendListModel {
        return RecommendListModel(
            totalCount, friends.map {
                RecommendListModel.RecommendFriend(
                    it.id,
                    it.name,
                    it.group,
                    it.profileImage
                )
            }
        )
    }
}