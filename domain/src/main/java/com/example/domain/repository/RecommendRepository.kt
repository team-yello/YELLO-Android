package com.example.domain.repository

import com.example.domain.entity.RecommendModel
import com.example.domain.entity.RequestRecommendKakaoModel

interface RecommendRepository {

    suspend fun postToGetKakaoFriendList(
        page: Int,
        request: RequestRecommendKakaoModel
    ): RecommendModel?

    suspend fun getSchoolFriendList(
        page: Int
    ): RecommendModel?

    suspend fun postFriendAdd(
        friendId: Long
    ): Unit

}