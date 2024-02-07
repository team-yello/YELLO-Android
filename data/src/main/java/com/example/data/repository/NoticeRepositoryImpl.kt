package com.example.data.repository

import com.example.data.datasource.NoticeDataSource
import com.example.domain.YelloDataStore
import com.example.domain.entity.notice.Banner
import com.example.domain.entity.notice.Notice
import com.example.domain.entity.notice.ProfileBanner
import com.example.domain.repository.NoticeRepository
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val dataSource: NoticeDataSource,
    private val yelloDataStore: YelloDataStore,
) : NoticeRepository {
    override suspend fun getNotice(): Result<Notice?> = runCatching {
        dataSource.getNotice().data?.toNotice()
    }

    override suspend fun getBanner(): Result<Banner?> = runCatching {
        dataSource.getBanner().data?.toBanner()
    }

    override suspend fun getProfileBanner(): Result<ProfileBanner?> = runCatching {
        dataSource.getProfileBanner().data?.toProfileBanner()
    }

    override fun isDisabledNoticeUrl(url: String): Boolean =
        yelloDataStore.disabledNoticeUrl == url

    override fun setDisabledNoticeUrl(url: String) {
        yelloDataStore.disabledNoticeUrl = url
    }
}
