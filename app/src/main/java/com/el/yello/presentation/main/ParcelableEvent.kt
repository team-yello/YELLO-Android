package com.el.yello.presentation.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableEvent(
    val title: String,
    val subTitle: String,
    val defaultLottieUrl: String,
    val openLottieUrl: String,
) : Parcelable
