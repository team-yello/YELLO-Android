package com.el.yello

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import okhttp3.OkHttpClient


private val flipperNetworkPlugin = NetworkFlipperPlugin()

fun Application.setUpFlipper() {
    if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
        SoLoader.init(this, false)
        val client = AndroidFlipperClient.getInstance(this).apply {
            addPlugin(InspectorFlipperPlugin(this@setUpFlipper, DescriptorMapping.withDefaults()))
            addPlugin(flipperNetworkPlugin)
            addPlugin(SharedPreferencesFlipperPlugin(this@setUpFlipper, packageName))
        }
        client.start()
    }
}

fun OkHttpClient.Builder.addFlipperNetworkPlugin(): OkHttpClient.Builder {
    return addNetworkInterceptor(FlipperOkhttpInterceptor(flipperNetworkPlugin))
}
