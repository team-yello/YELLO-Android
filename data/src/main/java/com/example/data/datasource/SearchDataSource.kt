package com.example.data.datasource

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.recommend.ResponseRecommendSearchDto

interface SearchDataSource {

    suspend fun getSearchListData(
        page: Int,
        keyword: String
    ): BaseResponse<ResponseRecommendSearchDto>

}
