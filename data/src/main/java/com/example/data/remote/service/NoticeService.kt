package com.example.data.remote.service

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.notice.ResponseGetNotice
import retrofit2.http.GET
import retrofit2.http.Path

interface NoticeService {
    @GET("/api/v1/notice/{tag}")
    suspend fun getNotice(
        @Path("tag") tag: String,
    ): BaseResponse<ResponseGetNotice>
}
