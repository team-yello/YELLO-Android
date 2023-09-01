package com.example.data.remote.service

import com.example.data.model.request.vote.RequestPostVoteDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.vote.ResponseGetFriendShuffleDto
import com.example.data.model.response.vote.ResponseGetVoteAvailableDto
import com.example.data.model.response.vote.ResponseGetVoteQuestionDto
import com.example.data.model.response.vote.ResponsePostVoteDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface VoteService {
    @GET("api/v1/vote/available")
    suspend fun getVoteAvailable(): BaseResponse<ResponseGetVoteAvailableDto>

    @GET("api/v1/friend/shuffle")
    suspend fun getFriendShuffle(): BaseResponse<List<ResponseGetFriendShuffleDto>>

    @GET("api/v1/vote/question")
    suspend fun getVoteQuestion(): BaseResponse<List<ResponseGetVoteQuestionDto>>

    @POST("api/v1/vote")
    suspend fun postVote(
        @Body requestPostVoteDto: RequestPostVoteDto,
    ): BaseResponse<ResponsePostVoteDto>
}
