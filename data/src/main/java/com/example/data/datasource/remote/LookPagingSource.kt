package com.example.data.datasource.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.remote.service.LookService
import com.example.domain.entity.LookListModel.LookModel
import timber.log.Timber

class LookPagingSource(
    private val lookService: LookService
) : PagingSource<Int, LookModel>() {

    override fun getRefreshKey(state: PagingState<Int, LookModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LookModel> {
        val currentPosition = params.key ?: 0
        val page = currentPosition.times(0.1).toInt()
        val response = runCatching { lookService.getLookList(page) }
            .getOrElse { return LoadResult.Error(it) }
        val nextPosition =
            if (response.data?.friendVotes?.isEmpty() == true) null else currentPosition + 10
        val previousPosition =
            if (currentPosition == 0) null else currentPosition - 1
        return runCatching {
            LoadResult.Page(
                data = response.data?.toLookListModel()?.friendVotes ?: listOf(),
                prevKey = previousPosition,
                nextKey = nextPosition
            )
        }.getOrElse {
            Timber.e(it)
            LoadResult.Error(it)
        }
    }
}