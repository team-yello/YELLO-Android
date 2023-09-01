package com.el.yello.presentation.util

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.el.yello.R
import com.google.android.material.button.MaterialButton

/**
 * made 2023.08.07
 * leekangmim
 */
class GradientButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        setupBackground()
        setupAnimation()
    }

    private fun setupBackground() {
        val colors = intArrayOf(ContextCompat.getColor(context, R.color.animate_start), ContextCompat.getColor(context, R.color.animate_center), ContextCompat.getColor(context, R.color.animate_end))
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            colors
        )
        gradientDrawable.cornerRadius = 100.dp.toFloat()
        gradientDrawable.setStroke(2.dp, ContextCompat.getColor(context, R.color.animate_start)) // 초기 테두리 색상

        background = gradientDrawable
    }

    private fun setupAnimation() {
        val animator = ObjectAnimator.ofArgb(this, "strokeColor", ContextCompat.getColor(context, R.color.animate_end), ContextCompat.getColor(context, R.color.white),  ContextCompat.getColor(context, R.color.animate_end))
        animator.duration = 2000 // 애니메이션 지속시간
        animator.repeatCount = ValueAnimator.INFINITE // 무한 반복

        animator.start()
    }

    // strokeColor 속성을 위한 setter 메서드
    fun setStrokeColor(color: Int) {
        val backgroundDrawable = background as? GradientDrawable
        backgroundDrawable?.setStroke(2.dp, color)
    }
}