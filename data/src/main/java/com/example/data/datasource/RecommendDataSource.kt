package com.example.data.datasource

import com.example.data.model.request.recommend.RequestRecommendKakaoDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.recommend.ResponseRecommendDto
import com.example.data.model.response.recommend.ResponseRecommendUserInfoDto

interface RecommendDataSource {
    suspend fun postToGetKakaoListData(
        page: Int,
        request: RequestRecommendKakaoDto,
    ): BaseResponse<ResponseRecommendDto>

    suspend fun getSchoolListData(
        page: Int,
    ): BaseResponse<ResponseRecommendDto>

    suspend fun postFriendAdd(
        friendId: Long,
    ): BaseResponse<Unit>

    suspend fun getRecommendUserInfo(
        userId: Long,
    ): BaseResponse<ResponseRecommendUserInfoDto>
}
