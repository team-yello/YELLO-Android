package com.example.domain.entity

data class ProfileUserModel(
    val userId: Long,
    val name: String,
    val profileImageUrl: String,
    val group: String,
    var yelloId: String,
    val yelloCount: Int,
    val friendCount: Int,
    val point: Int
){
    constructor() : this(0, "", "", "", "", 0, 0, 0)
}