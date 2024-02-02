package com.example.data.remote.service

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.recommend.ResponseRecommendSearchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("/api/v1/friend/search")
    suspend fun getSearchList(
        @Query("page") page: Int,
        @Query("keyword") keyword: String,
    ): BaseResponse<ResponseRecommendSearchDto>
}
