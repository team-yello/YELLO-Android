package com.example.domain.repository

import com.example.domain.entity.RecommendListModel
import com.example.domain.entity.RecommendRequestModel
import com.example.domain.entity.RecommendUserInfoModel

interface RecommendRepository {

    suspend fun postToGetKakaoFriendList(
        page: Int,
        request: RecommendRequestModel,
    ): Result<RecommendListModel?>

    suspend fun getSchoolFriendList(
        page: Int,
    ): Result<RecommendListModel?>

    suspend fun postFriendAdd(
        friendId: Long,
    ): Result<Unit>

    suspend fun getRecommendUserInfo(
        userId: Long,
    ): Result<RecommendUserInfoModel?>
}
