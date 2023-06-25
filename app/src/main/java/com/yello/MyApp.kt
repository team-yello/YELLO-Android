package com.yello

import android.app.Application

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        setUpFlipper()
    }
}