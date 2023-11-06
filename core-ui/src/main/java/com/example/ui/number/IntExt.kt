package com.example.ui.number

import android.content.Context
import android.util.TypedValue

fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        context.resources.displayMetrics,
    ).toInt()
}