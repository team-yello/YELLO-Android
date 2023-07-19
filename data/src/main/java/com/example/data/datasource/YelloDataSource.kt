package com.example.data.datasource

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.yello.ResponseCheckKeyword
import com.example.data.model.response.yello.ResponseCheckName
import com.example.data.model.response.yello.ResponseMyYello
import com.example.data.model.response.yello.ResponseYelloDetail

interface YelloDataSource {
    suspend fun getMyYelloList(page: Int): BaseResponse<ResponseMyYello>
    suspend fun getYelloDetail(id: Long): BaseResponse<ResponseYelloDetail>
    suspend fun checkKeyword(id: Long): BaseResponse<ResponseCheckKeyword>
    suspend fun checkName(id: Long): BaseResponse<ResponseCheckName>
}
