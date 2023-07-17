package com.example.data.datasource

import com.example.data.model.request.recommend.RequestRecommendKakaoDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.recommend.ResponseRecommendDto

interface RecommendDataSource {
    suspend fun  postToGetKakaoListData(
        accessToken: String,
        page: Int,
        request: RequestRecommendKakaoDto
    ): BaseResponse<List<ResponseRecommendDto>>

    suspend fun getSchoolListData(
        accessToken: String,
        page: Int
    ): BaseResponse<List<ResponseRecommendDto>>

}
