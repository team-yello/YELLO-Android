package com.example.data.repository

import com.example.data.datasource.SearchDataSource
import com.example.domain.entity.SearchListModel
import com.example.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchDataSource: SearchDataSource,
) : SearchRepository {

    override suspend fun getSearchList(
        page: Int, keyword: String
    ): Result<SearchListModel?> {
        return runCatching {
            searchDataSource.getSearchListData(
                page,
                keyword,
            ).data?.toSearchListModel()
        }
    }

}
