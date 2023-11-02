package com.example.data.model.response.recommend

import com.example.domain.entity.SearchListModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRecommendSearchDto(
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("friendList") val friendList: List<ResponseSearchFriendDto>
) {
    @Serializable
    data class ResponseSearchFriendDto(
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String,
        @SerialName("group") val group: String,
        @SerialName("profileImage") val profileImage: String,
        @SerialName("yelloId") val yelloId: String,
        @SerialName("isFriend") val isFriend: Boolean
    )

    fun toSearchListModel(): SearchListModel {
        return SearchListModel(totalCount, friendList.map {
            SearchListModel.SearchFriendModel(
                it.id, it.name, it.group, it.profileImage, it.yelloId, it.isFriend
            )
        })
    }
}