package com.yello

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.yello.BuildConfig.NATIVE_APP_KEY
import com.yello.presentation.util.ResolutionMetrics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {
    @Inject
    lateinit var metrics: ResolutionMetrics

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        setUpFlipper()
        KakaoSdk.init(this, NATIVE_APP_KEY)
        MyApp.resolutionMetrics = metrics
    }

    companion object {
        lateinit var resolutionMetrics: ResolutionMetrics
    }
}
