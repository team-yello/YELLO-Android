package com.el.yello

import android.app.Application
import com.amplitude.api.Amplitude
import com.el.yello.BuildConfig.NATIVE_APP_KEY
import com.el.yello.presentation.util.ResolutionMetrics
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.internal.Contexts.getApplication
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

        Amplitude.getInstance().initialize(this, "AMPLITUDE_KEY")
            .enableForegroundTracking(this)
    }

    companion object {
        lateinit var resolutionMetrics: ResolutionMetrics
    }
}
