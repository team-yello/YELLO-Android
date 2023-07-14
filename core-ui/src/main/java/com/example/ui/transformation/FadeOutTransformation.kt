package com.example.ui.transformation

import android.graphics.Color
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class FadeOutTransformation : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.translationX = -position * page.width
        page.setBackgroundColor(Color.TRANSPARENT)
        page.alpha = 1 - abs(position)
    }
}
