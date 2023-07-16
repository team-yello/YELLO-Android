package com.example.data.remote.service

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.vote.ResponseGetFriendShuffleDto
import com.example.data.model.response.vote.ResponseGetVoteAvailableDto
import retrofit2.http.GET

interface VoteService {
    @GET("api/v1/vote/available")
    suspend fun getVoteAvailable(): BaseResponse<ResponseGetVoteAvailableDto>

    @GET("/api/v1/friend/shuffle")
    suspend fun getFriendShuffle(): BaseResponse<List<ResponseGetFriendShuffleDto>>
}
