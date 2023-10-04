package com.example.domain.entity

data class RecommendSearchModel(
    val totalCount: Int,
    val friendList: List<SearchFriendModel>
) {
    data class SearchFriendModel(
        val id: Int,
        val name: String,
        val group: String,
        val profileImage: String,
        val yelloId: String,
        val isFriend: Boolean
    )
}