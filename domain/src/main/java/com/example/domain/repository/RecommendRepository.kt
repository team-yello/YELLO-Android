package com.example.domain.repository

import com.example.domain.entity.RecommendListModel
import com.example.domain.entity.SearchListModel
import com.example.domain.entity.RecommendRequestModel

interface RecommendRepository {

    suspend fun postToGetKakaoFriendList(
        page: Int,
        request: RecommendRequestModel,
    ): Result<RecommendListModel?>

    suspend fun getSchoolFriendList(
        page: Int,
    ): Result<RecommendListModel?>

    suspend fun postFriendAdd(
        friendId: Long,
    ): Result<Unit>

}
