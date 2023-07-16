package com.example.data.datasource.remote

import com.example.data.datasource.VoteDataSource
import com.example.data.model.request.RequestPostVoteDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.vote.ResponseGetFriendShuffleDto
import com.example.data.model.response.vote.ResponseGetVoteAvailableDto
import com.example.data.model.response.vote.ResponseGetVoteQuestionDto
import com.example.data.model.response.vote.ResponsePostVoteDto
import com.example.data.remote.service.VoteService
import javax.inject.Inject

class VoteDataSourceImpl @Inject constructor(
    private val service: VoteService,
) : VoteDataSource {
    override suspend fun getVoteAvailable(): BaseResponse<ResponseGetVoteAvailableDto> =
        service.getVoteAvailable()

    override suspend fun getFriendShuffle(): BaseResponse<List<ResponseGetFriendShuffleDto>> =
        service.getFriendShuffle()

    override suspend fun getVoteQuestion(): BaseResponse<List<ResponseGetVoteQuestionDto>> =
        service.getVoteQuestion()

    override suspend fun postVote(requestPostVoteDto: RequestPostVoteDto): BaseResponse<ResponsePostVoteDto> =
        service.postVote(requestPostVoteDto)
}
