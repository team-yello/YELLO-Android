package com.yello

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.yello.BuildConfig.NATIVE_APP_KEY
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        setUpFlipper()
        KakaoSdk.init(this, NATIVE_APP_KEY)
    }
}
