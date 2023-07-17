package com.example.domain.repository

import com.example.domain.entity.RecommendModel
import com.example.domain.entity.RequestRecommendKakaoModel
import com.example.domain.entity.RequestServiceTokenModel

interface RecommendRepository {

    suspend fun postToGetKakaoList(
        accessToken: String,
        page: Int,
        request: RequestRecommendKakaoModel
    ): List<RecommendModel>

}