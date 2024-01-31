package com.example.data.repository

import com.example.data.datasource.NoticeDataSource
import com.example.domain.YelloDataStore
import com.example.domain.entity.notice.Notice
import com.example.domain.repository.NoticeRepository
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val dataSource: NoticeDataSource,
    private val yelloDataStore: YelloDataStore,
) : NoticeRepository {
    override suspend fun getNotice(): Result<Notice?> = runCatching {
        dataSource.getNotice().data?.toNotice()
    }

    override fun isDisabledNoticeUrl(url: String): Boolean =
        yelloDataStore.disabledNoticeUrl == url

    override fun setDisabledNoticeUrl(url: String) {
        yelloDataStore.disabledNoticeUrl = url
    }
}
