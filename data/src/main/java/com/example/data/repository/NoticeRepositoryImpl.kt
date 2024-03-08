package com.example.data.repository

import androidx.core.content.edit
import com.example.data.datasource.NoticeDataSource
import com.example.data.local.YelloDataStoreImpl
import com.example.data.local.YelloDataStoreImpl.Companion.FORMAT_NOTICE_DISABLED_DATE
import com.example.domain.YelloDataStore
import com.example.domain.entity.notice.Banner
import com.example.domain.entity.notice.Notice
import com.example.domain.entity.notice.ProfileBanner
import com.example.domain.repository.NoticeRepository
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    override fun isDisabledNoticeUrl(url: String): Boolean {
        if (yelloDataStore.disabledNoticeUrl != url) return false
        val dateFormat = SimpleDateFormat(FORMAT_NOTICE_DISABLED_DATE, Locale.KOREA)
        val todayDate = dateFormat.format(Date())
        return todayDate == yelloDataStore.disabledNoticeDate
    }

    override fun setDisabledNoticeUrl(url: String) {
        yelloDataStore.disabledNoticeUrl = url
        SimpleDateFormat(FORMAT_NOTICE_DISABLED_DATE, Locale.KOREA).apply {
            yelloDataStore.disabledNoticeDate = format(Date())
        }
    }
}
