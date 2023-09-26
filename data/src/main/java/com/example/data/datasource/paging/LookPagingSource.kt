package com.example.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.remote.service.LookService
import com.example.domain.entity.ResponseLookListModel.LookModel
import javax.inject.Inject

class LookPagingSource @Inject constructor(
    private val lookService: LookService
) : PagingSource<Int, LookModel>() {

    // 리사이클러뷰의 스크롤 위치와 가장 가까운 페이지 찾아 이전 / 다음 페이지 결정
    override fun getRefreshKey(state: PagingState<Int, LookModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    // 현재 페이지의 정보 로드
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LookModel> {
        val currentPosition = params.key ?: 0
        val currentPage = currentPosition.times(LOOK_PAGING_POSITION).toInt()
        val response = runCatching {
            lookService.getLookList(currentPage)
        }.getOrElse {
            return LoadResult.Error(it)
        }
        val nextPosition =
            if (response.data?.friendVotes?.isEmpty() == true) null else currentPosition + LOOK_PAGING_SIZE
        val previousPosition =
            if (currentPosition == 0) null else currentPosition - 1
        return runCatching {
            LoadResult.Page(
                data = response.data?.toLookListModel()?.friendVotes ?: listOf(),
                prevKey = previousPosition,
                nextKey = nextPosition
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }

    companion object {
        const val LOOK_PAGING_SIZE = 10
        const val LOOK_PAGING_POSITION = 0.1
    }
}