package com.example.data.model.response.notice

import com.example.domain.entity.notice.Banner
import com.example.domain.entity.notice.Notice
import com.example.domain.entity.notice.ProfileBanner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetNoticeDto(
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("redirectUrl")
    val redirectUrl: String,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("endDate")
    val endDate: String,
    @SerialName("isAvailable")
    val isAvailable: Boolean,
    @SerialName("type")
    val type: String,
    @SerialName("title")
    val title: String,
) {
    fun toNotice(): Notice = Notice(
        imageUrl = imageUrl,
        redirectUrl = redirectUrl,
        isAvailable = isAvailable,
    )

    fun toBanner(): Banner = Banner(
        title = title,
        redirectUrl = redirectUrl,
        isAvailable = isAvailable,
    )

    fun toProfileBanner() : ProfileBanner = ProfileBanner(
        imageUrl = imageUrl,
        redirectUrl = redirectUrl,
        isAvailable = isAvailable
    )
}
