package com.example.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.remote.service.LookService
import com.example.domain.entity.LookListModel.LookModel
import javax.inject.Inject

class LookPagingSource @Inject constructor(
    private val lookService: LookService
) : PagingSource<Int, LookModel>() {

    // 리사이클러뷰의 스크롤 위치와 가장 가까운 페이지 찾아 이전 / 다음 페이지 결정
    override fun getRefreshKey(state: PagingState<Int, LookModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(LOOK_PAGE_SIZE) ?: anchorPage?.nextKey?.minus(LOOK_PAGE_SIZE)
        }
    }

    // 현재 페이지의 정보 로드
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LookModel> {
        val currentPosition = params.key ?: 0
        val currentPage = currentPosition.times(LOOK_POSITION_TO_PAGE).toInt()
        val response = runCatching {
            lookService.getLookList(currentPage)
        }.getOrElse {
            return LoadResult.Error(it)
        }
        val nextPosition =
            if (response.data?.friendVotes.isNullOrEmpty()) null else currentPosition + LOOK_PAGE_SIZE
        val previousPosition =
            if (currentPosition == 0) null else currentPosition - LOOK_PAGE_SIZE
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
        const val LOOK_PAGE_SIZE = 10
        const val LOOK_POSITION_TO_PAGE = 0.1
    }
}