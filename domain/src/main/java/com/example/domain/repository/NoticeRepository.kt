package com.example.domain.repository

import com.example.domain.entity.notice.Banner
import com.example.domain.entity.notice.Notice

interface NoticeRepository {
    suspend fun getNotice(): Result<Notice?>

    suspend fun getBanner(): Result<Banner?>

    fun isDisabledNoticeUrl(url: String): Boolean

    fun setDisabledNoticeUrl(url: String)
}
