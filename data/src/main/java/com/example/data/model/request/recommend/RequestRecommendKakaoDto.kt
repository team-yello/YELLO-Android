package com.example.data.model.request.recommend

import com.example.domain.entity.RecommendRequestModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestRecommendKakaoDto(
    @SerialName("friendKakaoId")
    val friendKakaoId: List<String>
)

fun RecommendRequestModel.toRequestDto(): RequestRecommendKakaoDto {
    return RequestRecommendKakaoDto(friendKakaoId)
}