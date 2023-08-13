package com.el.yello.presentation.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.el.yello.R

/**
 * made 2023.08.07
 * leekangmim
 */
class GradientStrokeButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var cornerRadius = 0f
    private var borderWidth = 0f
    private var startColor = 0
    private var centerColor = 0
    private var endColor = 0

    private val path = Path()
    private val borderPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    init {
        //Get the values you set in xml
        context.withStyledAttributes(attrs, R.styleable.StyledButton) {
            borderWidth = getDimension(R.styleable.StyledButton_borderWidth, 2.dp.toFloat())
            cornerRadius = getDimension(R.styleable.StyledButton_cornerRadius, 100.dp.toFloat())
            startColor = getColor(
                R.styleable.StyledButton_startColor,
                ContextCompat.getColor(context, R.color.animate_start)
            )
            centerColor = getColor(
                R.styleable.StyledButton_centerColor,
                ContextCompat.getColor(context, R.color.animate_center)
            )
            endColor = getColor(
                R.styleable.StyledButton_endColor,
                ContextCompat.getColor(context, R.color.animate_end)
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Create and set your gradient here so that the gradient size is always correct
        borderPaint.shader = LinearGradient(
            45f,
            45f,
            width.toFloat(),
            height.toFloat(),
            intArrayOf(startColor, centerColor, endColor),
            null,
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //Remove inner section (that you require to be transparent) from canvas
        path.rewind()
        path.addRoundRect(
            borderWidth,
            borderWidth,
            width.toFloat() - borderWidth,
            height.toFloat() - borderWidth,
            cornerRadius - borderWidth / 2,
            cornerRadius - borderWidth / 2,
            Path.Direction.CCW
        )
        canvas.clipOutPath(path)

        //Draw gradient on the outer section
        path.rewind()
        path.addRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            cornerRadius,
            cornerRadius,
            Path.Direction.CCW
        )
        canvas.drawPath(path, borderPaint)
    }
}


