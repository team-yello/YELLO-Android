package com.example.data.repository

import com.example.data.datasource.RecommendDataSource
import com.example.data.model.request.recommend.toRequestDto
import com.example.domain.entity.RecommendModel
import com.example.domain.entity.RecommendSearchModel
import com.example.domain.entity.RequestRecommendKakaoModel
import com.example.domain.repository.RecommendRepository
import javax.inject.Inject

class RecommendRepositoryImpl @Inject constructor(
    private val recommendDataSource: RecommendDataSource,
) : RecommendRepository {

    override suspend fun postToGetKakaoFriendList(
        page: Int,
        request: RequestRecommendKakaoModel,
    ): Result<RecommendModel?> {
        return runCatching {
            recommendDataSource.postToGetKakaoListData(
                page,
                request.toRequestDto(),
            ).data?.toRecommendModel()
        }
    }

    override suspend fun getSchoolFriendList(
        page: Int
    ): Result<RecommendModel?> {
        return runCatching {
            recommendDataSource.getSchoolListData(
                page,
            ).data?.toRecommendModel()
        }
    }

    override suspend fun postFriendAdd(
        friendId: Long
    ): Result<Unit> {
        return runCatching {
            recommendDataSource.postFriendAdd(friendId).data
        }
    }

    override suspend fun getSearchList(
        page: Int, keyword: String
    ): Result<RecommendSearchModel?> {
        return runCatching {
            recommendDataSource.getSearchListData(
                page,
                keyword,
            ).data?.toRecommendSearchModel()
        }
    }

}
