package com.el.yello

import android.app.Application
import com.el.yello.BuildConfig.NATIVE_APP_KEY
import com.el.yello.presentation.util.ResolutionMetrics
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application() {
    @Inject
    lateinit var metrics: ResolutionMetrics

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        setUpFlipper()
        KakaoSdk.init(this, NATIVE_APP_KEY)
        resolutionMetrics = metrics
    }

    companion object {
        lateinit var resolutionMetrics: ResolutionMetrics
    }
}
