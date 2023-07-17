package com.example.domain.entity

data class RecommendModel(
    val status : Int,
    val message : String,
    val totalCount : Int,
    val data: List<RecommendFriend>
) {
    data class RecommendFriend(
        val id: Int,
        val name: String,
        val group: String,
        val profileImage: String?
    )
}