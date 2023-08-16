package com.example.data.remote.service

import com.example.data.model.request.RequestPayIndexDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.ResponseDto
import com.example.data.model.response.vote.ResponseVoteCount
import com.example.data.model.response.yello.ResponseCheckKeyword
import com.example.data.model.response.yello.ResponseCheckName
import com.example.data.model.response.yello.ResponseMyYello
import com.example.data.model.response.yello.ResponseYelloDetail
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface YelloService {
    @GET("api/v1/vote")
    suspend fun getMyYelloList(
        @Query("page") page: Int,
    ): BaseResponse<ResponseMyYello>

    @GET("api/v1/vote/{id}")
    suspend fun getYelloDetail(
        @Path("id") id: Long,
    ): BaseResponse<ResponseYelloDetail>

    @PATCH("api/v1/vote/{id}/keyword")
    suspend fun checkKeyword(
        @Path("id") id: Long,
    ): BaseResponse<ResponseCheckKeyword>

    @PATCH("api/v1/vote/{id}/name")
    suspend fun checkName(
        @Path("id") id: Long,
    ): BaseResponse<ResponseCheckName>

    @POST("api/v1/pay")
    suspend fun payCheck(
        @Body body: RequestPayIndexDto
    ): ResponseDto

    @GET("api/v1/vote/count")
    suspend fun voteCount(): BaseResponse<ResponseVoteCount>
}