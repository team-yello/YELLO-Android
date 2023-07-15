package com.example.data.datasource.remote

import com.example.data.datasource.VoteDataSource
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.vote.ResponseGetVoteAvailableDto
import com.example.data.remote.service.VoteService
import javax.inject.Inject

class VoteDataSourceImpl @Inject constructor(
    private val service: VoteService,
) : VoteDataSource {
    override suspend fun getVoteAvailable(): BaseResponse<ResponseGetVoteAvailableDto> =
        service.getVoteAvailable()
}
