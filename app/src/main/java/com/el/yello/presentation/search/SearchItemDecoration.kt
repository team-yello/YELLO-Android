package com.el.yello.presentation.search

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.example.ui.intent.dpToPx

class SearchItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val dividerHeight = dpToPx(context, 1)
    private val dividerMargin = dpToPx(context, 8)
    private val dividerColor = ContextCompat.getColor(context, R.color.grayscales_800)
    private val dividerPaint = Paint()

    init {
        dividerPaint.color = dividerColor
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dividerHeight
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + dividerMargin
        val right = parent.width - parent.paddingRight - dividerMargin

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + dividerHeight

            c.drawRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                bottom.toFloat(),
                dividerPaint,
            )
        }
    }
}