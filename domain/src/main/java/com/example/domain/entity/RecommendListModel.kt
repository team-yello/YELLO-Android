package com.example.domain.entity

data class RecommendListModel(
    val totalCount: Int,
    val friends: List<RecommendFriend>,
) {
    data class RecommendFriend(
        val id: Int,
        val name: String,
        val group: String,
        val profileImage: String?,
    )
}
