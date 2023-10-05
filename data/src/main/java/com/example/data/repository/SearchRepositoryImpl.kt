package com.example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.datasource.paging.SearchPagingSource
import com.example.data.datasource.paging.SearchPagingSource.Companion.SEARCH_PAGE_SIZE
import com.example.data.remote.service.SearchService
import com.example.domain.entity.SearchListModel.SearchFriendModel
import com.example.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchService: SearchService
) : SearchRepository {

    override fun getSearchList(keyword: String): Flow<PagingData<SearchFriendModel>> {
        return Pager(
            config = PagingConfig(SEARCH_PAGE_SIZE),
            pagingSourceFactory = { SearchPagingSource(searchService, keyword) }
        ).flow
    }

}
