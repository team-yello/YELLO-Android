package com.el.yello.util.extension

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ui.extension.colorOf

fun SwipeRefreshLayout.setPullToScrollColor(arrowColor: Int, bgColor: Int) {
    setColorSchemeColors(context.colorOf(arrowColor))
    setProgressBackgroundColorSchemeColor(context.colorOf(bgColor))
}