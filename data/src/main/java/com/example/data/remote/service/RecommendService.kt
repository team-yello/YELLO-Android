package com.example.data.remote.service

import com.example.data.model.request.recommend.RequestRecommendKakaoDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.recommend.ResponseRecommendDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RecommendService {

    @POST("/api/v1/friend/recommend/kakao")
    suspend fun postToGetKakaoList(
        @Query("page") page: Int,
        @Body request: RequestRecommendKakaoDto
    ): BaseResponse<List<ResponseRecommendDto>>

    @GET("/api/v1/friend/recommend/school")
    suspend fun getSchoolList(
        @Query("page") page: Int
    ): BaseResponse<List<ResponseRecommendDto>>

}