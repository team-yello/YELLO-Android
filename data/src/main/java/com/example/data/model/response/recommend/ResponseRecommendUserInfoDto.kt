package com.example.data.model.response.recommend

import com.example.domain.entity.RecommendUserInfoModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRecommendUserInfoDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("name")
    val name: String,
    @SerialName("profileImageUrl")
    val profileImageUrl: String,
    @SerialName("group")
    val group: String,
    @SerialName("yelloId")
    val yelloId: String,
    @SerialName("yelloCount")
    val yelloCount: Int,
    @SerialName("friendCount")
    val friendCount: Int,
) {

    fun toRecommendUserModel(): RecommendUserInfoModel {
        return RecommendUserInfoModel(
            userId,
            name,
            profileImageUrl,
            group,
            yelloId,
            yelloCount,
            friendCount,
        )
    }
}
