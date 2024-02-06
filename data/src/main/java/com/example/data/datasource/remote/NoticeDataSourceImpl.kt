package com.example.data.datasource.remote

import com.example.data.datasource.NoticeDataSource
import com.example.data.model.response.BaseResponse
import com.example.data.model.response.notice.ResponseGetNotice
import com.example.data.remote.service.NoticeService
import javax.inject.Inject

class NoticeDataSourceImpl @Inject constructor(
    private val service: NoticeService,
) : NoticeDataSource {
    override suspend fun getNotice(): BaseResponse<ResponseGetNotice> =
        service.getNotice(TAG_NOTICE)

    override suspend fun getBanner(): BaseResponse<ResponseGetNotice> =
        service.getNotice(TAG_BANNER)

    companion object {
        private const val TAG_NOTICE = "NOTICE"
        private const val TAG_BANNER = "BANNER"
    }
}
