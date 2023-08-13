package com.example.data.datasource.remote

import com.example.data.datasource.RecommendDataSource
import com.example.data.model.request.recommend.RequestRecommendKakaoDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.recommend.ResponseRecommendDto
import com.example.data.model.response.recommend.ResponseRecommendSearchDto
import com.example.data.remote.service.RecommendService
import javax.inject.Inject

class RecommendDataSourceImpl @Inject constructor(
    private val recommendService: RecommendService
) : RecommendDataSource {

    override suspend fun postToGetKakaoListData(
        page: Int,
        request: RequestRecommendKakaoDto
    ): BaseResponse<ResponseRecommendDto> {
        return recommendService.postToGetKakaoList(page, request)
    }

    override suspend fun getSchoolListData(
        page: Int
    ): BaseResponse<ResponseRecommendDto> {
        return recommendService.getSchoolList(page)
    }

    override suspend fun postFriendAdd(
        friendId: Long
    ): BaseResponse<Unit> {
        return recommendService.postFriendAdd(friendId)
    }

    override suspend fun getSearchListData(
        page: Int,
        keyword: String
    ): BaseResponse<ResponseRecommendSearchDto> {
        return recommendService.getSearchList(page, keyword)
    }

}