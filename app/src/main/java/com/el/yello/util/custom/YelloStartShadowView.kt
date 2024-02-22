package com.el.yello.util.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class YelloStartShadowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private val rect = RectF()

    private val eraser = Paint().apply {
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawHole(requireNotNull(canvas))
    }

    private fun drawHole(canvas: Canvas) {
        val holeBorderRadius = width / 2f

        canvas.drawCircle(holeBorderRadius, holeBorderRadius, holeBorderRadius, eraser)
        canvas.drawRect(
            rect.apply {
                setRect()
            },
            eraser,
        )
    }

    private fun setRect() {
        val holeWidth = width
        val holeHeight = width / 2f

        rect.set(0f, 0f, holeWidth.toFloat(), holeHeight)
    }
}
