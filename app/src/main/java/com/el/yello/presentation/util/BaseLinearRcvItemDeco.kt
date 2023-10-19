package com.el.yello.presentation.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BaseLinearRcvItemDeco(
    private val top: Int = 0,
    private val bottom: Int = 0,
    private val right: Int = 0,
    private val left: Int = 0,
    private val startPadding: Int = 0,
    private val orientation: Int = RecyclerView.VERTICAL,
    private val bottomPadding: Int = 0,
) : RecyclerView.ItemDecoration() {

    constructor(
        top: Int,
        bottom: Int,
        right: Int,
        left: Int,
        startPadding: Int,
        orientation: Int,
    ) : this(top, bottom, right, left, startPadding, orientation, -1)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        // 현재 아이템의 포지션 가져오기
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (orientation == RecyclerView.VERTICAL) {
            outRect.right = right.dp
            outRect.left = left.dp

            when (position) {
                0 -> {
                    outRect.top = startPadding.dp
                    outRect.bottom = bottom.dp / 2
                }

                itemCount - 1 -> {
                    outRect.top = top.dp / 2
                    outRect.bottom = if (bottomPadding >= 0) bottomPadding.dp else bottom.dp
                }

                else -> {
                    outRect.top = top.dp / 2
                    outRect.bottom = bottom.dp / 2
                }
            }
        } else if (orientation == RecyclerView.HORIZONTAL) {
            outRect.top = top.dp
            outRect.bottom = bottom.dp

            when (position) {
                0 -> {
                    outRect.left = startPadding.dp
                    outRect.right = right.dp / 2
                }

                itemCount - 1 -> {
                    outRect.left = left.dp / 2
                    if (bottomPadding >= 0) {
                        outRect.right = bottomPadding.dp
                    }
                }

                else -> {
                    outRect.right = right.dp / 2
                    outRect.left = left.dp / 2
                }
            }
        }
    }
}
