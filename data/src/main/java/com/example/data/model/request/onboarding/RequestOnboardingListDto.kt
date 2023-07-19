package com.example.data.model.request.onboarding

import com.example.data.model.request.recommend.RequestRecommendKakaoDto
import com.example.domain.entity.RequestOnboardingListModel
import com.example.domain.entity.RequestRecommendKakaoModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestOnboardingListDto(
    @SerialName("friendKaKaoId")
    val friendKaKaoId: List<String>,
    @SerialName("groupId")
    val groupId: Long,
)

fun RequestOnboardingListModel.toRequestDto() = RequestOnboardingListDto(
    friendKaKaoId = friendKakaoId,
    groupId = groupId,
)