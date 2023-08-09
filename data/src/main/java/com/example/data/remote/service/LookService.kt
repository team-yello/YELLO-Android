package com.example.data.remote.service

import com.example.data.model.response.look.ResponseLookListDto
import retrofit2.http.GET

interface LookService {

    @GET("/api/v1/vote/friend")
    suspend fun getLookList(
    ): ResponseLookListDto

}