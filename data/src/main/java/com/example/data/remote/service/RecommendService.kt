package com.example.data.remote.service

import com.example.data.model.request.recommend.RequestRecommendKakaoDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.recommend.ResponseRecommendDto
import com.example.data.model.response.recommend.ResponseRecommendSearchDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecommendService {

    @POST("/api/v1/friend/recommend/kakao")
    suspend fun postToGetKakaoList(
        @Query("page") page: Int,
        @Body request: RequestRecommendKakaoDto
    ): BaseResponse<ResponseRecommendDto>

    @GET("/api/v1/friend/recommend/school")
    suspend fun getSchoolList(
        @Query("page") page: Int
    ): BaseResponse<ResponseRecommendDto>

    @POST("/api/v1/friend/{friendId}")
    suspend fun postFriendAdd(
        @Path("friendId") friendId: Long
    ): BaseResponse<Unit>

}