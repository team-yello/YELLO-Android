package com.el.yello

import android.app.Application
import com.amplitude.api.Amplitude
import com.el.yello.BuildConfig.AMPLITUDE_API_KEY
import com.el.yello.BuildConfig.NATIVE_APP_KEY
import com.el.yello.presentation.util.ResolutionMetrics
import com.google.android.gms.ads.MobileAds
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

        Amplitude.getInstance().initialize(this, AMPLITUDE_API_KEY)
            .enableForegroundTracking(this)

        MobileAds.initialize(this) {}
    }

    companion object {
        lateinit var resolutionMetrics: ResolutionMetrics
    }
}
