package com.example.data.model.request.onboarding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestSignFriendDto(
    @SerialName("friendKaKaoId")
    val friendKaKaoId: List<String>,

)
