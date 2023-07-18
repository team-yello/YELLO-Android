package com.example.data.remote.service

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.yello.ResponseCheckKeyword
import com.example.data.model.response.yello.ResponseCheckName
import com.example.data.model.response.yello.ResponseMyYello
import com.example.data.model.response.yello.ResponseYelloDetail
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface YelloService {
    @GET("api/v1/vote")
    suspend fun getMyYelloList(
        @Query("page") page: Int
    ): BaseResponse<ResponseMyYello>

    @GET("api/v1/vote/{id}")
    suspend fun getYelloDetail(
        @Path("id") id: Long
    ): BaseResponse<ResponseYelloDetail>

    @POST("/api/v1/yello/{id}/keyword")
    suspend fun checkKeyword(
        @Path("id") id: Long
    ): BaseResponse<ResponseCheckKeyword>

    @POST("/api/v1/yello/{id}/name")
    suspend fun checkName(
        @Path("id") id: Long
    ): BaseResponse<ResponseCheckName>
}