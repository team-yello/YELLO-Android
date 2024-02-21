package com.el.yello.presentation.main

import android.app.Application
import androidx.annotation.Px
import com.el.yello.MyApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.roundToInt

class ResolutionMetrics @Inject constructor(
    @ApplicationContext private val application: Application,
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
