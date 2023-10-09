package com.example.domain.repository

import com.example.domain.entity.RecommendModel
import com.example.domain.entity.RecommendSearchModel
import com.example.domain.entity.RequestRecommendKakaoModel

interface RecommendRepository {

    suspend fun postToGetKakaoFriendList(
        page: Int,
        request: RequestRecommendKakaoModel,
    ): Result<RecommendModel?>

    suspend fun getSchoolFriendList(
        page: Int,
    ): Result<RecommendModel?>

    suspend fun postFriendAdd(
        friendId: Long,
    ): Result<Unit>

    suspend fun getSearchList(
        page: Int,
        keyword: String
    ): Result<RecommendSearchModel?>

}
