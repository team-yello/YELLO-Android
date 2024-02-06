package com.example.data.datasource

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.notice.ResponseGetNotice

interface NoticeDataSource {
    suspend fun getNotice(): BaseResponse<ResponseGetNotice>

    suspend fun getBanner(): BaseResponse<ResponseGetNotice>
}
