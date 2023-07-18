package com.example.domain.entity.onboarding

import com.example.domain.enum.GenderEnum

data class SignupInfo(
    val kakaoId: Int,
    val email: String,
    val profileImg: String,
    val groupId: Long,
    val studentId: Int,
    val name: String,
    val yelloId: String,
    val gender: GenderEnum,
    val friendList: List<Long>,
    val recommendId: String? = null,
)
