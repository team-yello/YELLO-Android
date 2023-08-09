package com.example.domain.entity.onboarding

data class RequestAddFriendModel(
    val friendKakaoId: List<String>,
    var groupId: Long
)
