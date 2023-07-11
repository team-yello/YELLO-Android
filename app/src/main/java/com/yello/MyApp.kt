package com.yello

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.yello.BuildConfig.NATIVE_APP_KEY
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        setUpFlipper()
        KakaoSdk.init(this, NATIVE_APP_KEY)
    }
}
