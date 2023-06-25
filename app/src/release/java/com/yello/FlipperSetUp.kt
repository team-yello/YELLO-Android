package com.yello

import android.app.Application
import okhttp3.OkHttpClient


fun Application.setUpFlipper() = 0

fun OkHttpClient.Builder.addFlipperNetworkPlugin(): OkHttpClient.Builder {
    return this
}