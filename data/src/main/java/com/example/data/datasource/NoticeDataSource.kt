package com.example.data.datasource

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.notice.ResponseGetNoticeDto

interface NoticeDataSource {
    suspend fun getNotice(): BaseResponse<ResponseGetNoticeDto>

    suspend fun getBanner(): BaseResponse<ResponseGetNoticeDto>

    suspend fun getProfileBanner(): BaseResponse<ResponseGetNoticeDto>
}
