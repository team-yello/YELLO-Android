package com.example.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.remote.service.SearchService
import com.example.domain.entity.SearchListModel.SearchFriendModel
import javax.inject.Inject

class SearchPagingSource @Inject constructor(
    private val searchService: SearchService, private val keyword: String
) : PagingSource<Int, SearchFriendModel>() {

    override fun getRefreshKey(state: PagingState<Int, SearchFriendModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(SEARCH_PAGE_SIZE) ?: anchorPage?.nextKey?.minus(
                SEARCH_PAGE_SIZE
            )
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchFriendModel> {
        val currentPosition = params.key ?: 0
        val currentPage = currentPosition.times(SEARCH_POSITION_TO_PAGE).toInt()
        val response = runCatching {
            searchService.getSearchList(currentPage, keyword)
        }.getOrElse {
            return LoadResult.Error(it)
        }
        val nextPosition =
            if (response.data?.friendList.isNullOrEmpty()) null else currentPosition + SEARCH_PAGE_SIZE
        val previousPosition =
            if (currentPosition == 0) null else currentPosition - SEARCH_PAGE_SIZE
        return runCatching {
            LoadResult.Page(
                data = response.data?.toSearchListModel()?.friendList ?: listOf(),
                prevKey = previousPosition,
                nextKey = nextPosition
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }

    companion object {
        const val SEARCH_PAGE_SIZE = 10
        const val SEARCH_POSITION_TO_PAGE = 0.1
    }
}