package com.example.domain.repository

import com.example.domain.entity.notice.Notice

interface NoticeRepository {
    suspend fun getNotice(): Result<Notice?>

    fun isDisabledNoticeUrl(url: String): Boolean

    fun setDisabledNoticeUrl(url: String)
}
