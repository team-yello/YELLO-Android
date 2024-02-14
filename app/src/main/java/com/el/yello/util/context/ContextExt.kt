package com.el.yello.util.context

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.el.yello.util.view.YelloSnackbar

fun Context.yelloSnackbar(anchorView: View, message: String) {
    YelloSnackbar.make(anchorView, message).show()
}

fun Fragment.yelloSnackbar(anchorView: View, message: String) {
    YelloSnackbar.make(anchorView, message).show()
}
