package com.example.data.datasource.remote

import com.example.data.datasource.SearchDataSource
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.recommend.ResponseRecommendSearchDto
import com.example.data.remote.service.SearchService
import javax.inject.Inject

class SearchDataSourceImpl @Inject constructor(
    private val searchService: SearchService
) : SearchDataSource {

    override suspend fun getSearchListData(
        page: Int,
        keyword: String
    ): BaseResponse<ResponseRecommendSearchDto> {
        return searchService.getSearchList(page, keyword)
    }

}