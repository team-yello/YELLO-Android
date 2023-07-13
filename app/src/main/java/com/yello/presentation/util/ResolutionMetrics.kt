package com.yello.presentation.util

import android.app.Application
import androidx.annotation.Px
import com.yello.MyApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.roundToInt

class ResolutionMetrics @Inject constructor(
    @ApplicationContext private val application: Application
) {
    private val displayMetrics
        get() = application.resources.displayMetrics

    val screenWidth
        get() = displayMetrics.widthPixels

    val screenHeight
        get() = displayMetrics.heightPixels

    val screenShort
        get() = screenWidth.coerceAtMost(screenHeight)

    val screenLong
        get() = screenWidth.coerceAtLeast(screenHeight)

    @Px
    fun toPixel(dp: Int) = (dp * displayMetrics.density).roundToInt()
    fun toDP(@Px pixel: Int) = (pixel / displayMetrics.density).roundToInt()
}

val Number.pixel: Int
    @Px get() = MyApp.resolutionMetrics.toDP(this.toInt())

val Number.dp: Int
    get() = MyApp.resolutionMetrics.toPixel(this.toInt())

val Number.pixelFloat: Float
    @Px get() = MyApp.resolutionMetrics.toDP(this.toInt()).toFloat()

val Number.dpFloat: Float
    get() = MyApp.resolutionMetrics.toPixel(this.toInt()).toFloat()