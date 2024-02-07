package com.example.domain.repository

import com.example.domain.entity.notice.Banner
import com.example.domain.entity.notice.Notice
import com.example.domain.entity.notice.ProfileBanner

interface NoticeRepository {
    suspend fun getNotice(): Result<Notice?>

    suspend fun getBanner(): Result<Banner?>

    suspend fun getProfileBanner(): Result<ProfileBanner?>

    fun isDisabledNoticeUrl(url: String): Boolean

    fun setDisabledNoticeUrl(url: String)
}
