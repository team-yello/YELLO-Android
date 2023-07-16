package com.example.data.datasource

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.vote.ResponseGetFriendShuffleDto
import com.example.data.model.response.vote.ResponseGetVoteAvailableDto

interface VoteDataSource {
    suspend fun getVoteAvailable(): BaseResponse<ResponseGetVoteAvailableDto>

    suspend fun getFriendShuffle(): BaseResponse<List<ResponseGetFriendShuffleDto>>
}
