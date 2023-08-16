package com.example.data.datasource.remote

import com.example.data.datasource.YelloDataSource
import com.example.data.model.request.RequestPayDto
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.ResponseDto
import com.example.data.model.response.vote.ResponseVoteCount
import com.example.data.model.response.yello.ResponseCheckKeyword
import com.example.data.model.response.yello.ResponseCheckName
import com.example.data.model.response.yello.ResponseMyYello
import com.example.data.model.response.yello.ResponseYelloDetail
import com.example.data.remote.service.YelloService
import javax.inject.Inject

class YelloDataSourceImpl @Inject constructor(
    private val service: YelloService,
) : YelloDataSource {
    override suspend fun getMyYelloList(page: Int): BaseResponse<ResponseMyYello> {
        return service.getMyYelloList(page)
    }

    override suspend fun getYelloDetail(id: Long): BaseResponse<ResponseYelloDetail> {
        return service.getYelloDetail(id)
    }

    override suspend fun checkKeyword(id: Long): BaseResponse<ResponseCheckKeyword> {
        return service.checkKeyword(id)
    }

    override suspend fun checkName(id: Long): BaseResponse<ResponseCheckName> {
        return service.checkName(id)
    }

    override suspend fun payCheck(index: Int): ResponseDto {
        return service.payCheck(RequestPayDto(index))
    }

    override suspend fun voteCount(): BaseResponse<ResponseVoteCount> {
        return service.voteCount()
    }
}
