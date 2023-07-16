package com.example.domain.repository

import com.example.domain.entity.RecommendModel
import com.example.domain.entity.RequestServiceTokenModel

interface RecommendRepository {

    suspend fun postToGetKakaoList(requestServiceTokenModel: RequestServiceTokenModel): List<RecommendModel>

}