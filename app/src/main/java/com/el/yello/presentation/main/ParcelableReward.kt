package com.el.yello.presentation.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableReward(
    val imageUrl: String,
    val name: String,
) : Parcelable
