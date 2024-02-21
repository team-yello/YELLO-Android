package com.el.yello.util.extension

import androidx.annotation.Px
import com.el.yello.MyApp

val Number.pixel: Int
    @Px get() = MyApp.resolutionMetrics.toDP(this.toInt())

val Number.dp: Int
    get() = MyApp.resolutionMetrics.toPixel(this.toInt())

val Number.pixelFloat: Float
    @Px get() = MyApp.resolutionMetrics.toDP(this.toInt()).toFloat()

val Number.dpFloat: Float
    get() = MyApp.resolutionMetrics.toPixel(this.toInt()).toFloat()