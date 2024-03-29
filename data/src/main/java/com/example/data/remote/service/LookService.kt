package com.example.data.remote.service

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.look.ResponseLookListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LookService {

    @GET("/api/v2/vote/friend")
    suspend fun getLookList(
        @Query("page") page: Int,
        @Query("type") type: String? = null
    ): BaseResponse<ResponseLookListDto>
}
