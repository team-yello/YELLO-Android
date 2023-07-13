package com.yello.util

import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.yello.R

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("setTextViewDrawable")
    fun EditText.setTextViewDrawable(input: String) {
        if (!input.matches("^[ㄱ-ㅎ가-힣]*\$".toRegex())) {
            setBackgroundResource(R.drawable.shape_fill_red20_line_semantic_status_red500_rect_8)
            return
        }
        if (isHovered) {
            setBackgroundResource(R.drawable.shape_grayscales900_fill_grayscales600_line_8_rect)
            return
        }
        setBackgroundResource(R.drawable.shape_black_fill_grayscales600_line_8_rect)
    }
}
