package com.example.domain.repository

import com.example.domain.entity.RecommendModel
import com.example.domain.entity.RequestRecommendKakaoModel

interface RecommendRepository {

    suspend fun postToGetKakaoFriendList(
        page: Int,
        request: RequestRecommendKakaoModel
    ): List<RecommendModel>

    suspend fun getSchoolFriendList(
        page: Int
    ): List<RecommendModel>

}