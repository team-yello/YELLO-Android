package com.example.data.datasource

import com.example.data.model.request.recommend.RequestRecommendKakaoDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.recommend.ResponseRecommendAddDto
import com.example.data.model.response.recommend.ResponseRecommendDto

interface RecommendDataSource {
    suspend fun postToGetKakaoListData(
        page: Int,
        request: RequestRecommendKakaoDto
    ): BaseResponse<List<ResponseRecommendDto>>

    suspend fun getSchoolListData(
        page: Int
    ): BaseResponse<List<ResponseRecommendDto>>

    suspend fun postFriendAdd(
        friendId: Long
    ): ResponseRecommendAddDto

}
