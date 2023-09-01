package com.example.domain.entity.onboarding

data class SignupInfo(
    val kakaoId: String,
    val email: String,
    val profileImg: String,
    val groupId: Long,
    val studentId: Int,
    val name: String,
    val yelloId: String,
    val gender: String,
    val friendList: List<Long>,
    val recommendId: String? = null,
    val deviceToken: String
)
