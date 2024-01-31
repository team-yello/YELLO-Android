package com.example.domain.entity

data class ProfileModRequestModel(
    val name: String,
    val yelloId: String,
    val gender: String,
    val email: String,
    var profileImageUrl: String,
    val groupId: Long,
    val groupAdmissionYear: Int
)