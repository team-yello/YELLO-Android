package com.example.data.datasource

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.vote.ResponseGetFriendShuffleDto
import com.example.data.model.response.vote.ResponseGetVoteAvailableDto
import com.example.data.model.response.vote.ResponseGetVoteQuestionDto

interface VoteDataSource {
    suspend fun getVoteAvailable(): BaseResponse<ResponseGetVoteAvailableDto>

    suspend fun getFriendShuffle(): BaseResponse<List<ResponseGetFriendShuffleDto>>

    suspend fun getVoteQuestion(): BaseResponse<List<ResponseGetVoteQuestionDto>>
}
