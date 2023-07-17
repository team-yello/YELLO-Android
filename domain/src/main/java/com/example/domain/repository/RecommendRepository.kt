package com.example.domain.repository

import com.example.domain.entity.RecommendModel
import com.example.domain.entity.RequestRecommendKakaoModel

interface RecommendRepository {

    suspend fun postToGetKakaoFriendList(
        accessToken: String,
        page: Int,
        request: RequestRecommendKakaoModel
    ): List<RecommendModel>

    suspend fun getSchoolFriendList(
        accessToken: String,
        page: Int
    ): List<RecommendModel>

}