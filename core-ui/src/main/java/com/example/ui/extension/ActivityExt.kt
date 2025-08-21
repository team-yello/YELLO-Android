package com.example.ui.extension

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

inline fun <reified T : Activity> Activity.navigateTo() {
    Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(this)
    }
}

/**
 * EdgeToEdge 인셋 처리를 위한 확장함수
 * 주어진 View에 대해 시스템 바와 디스플레이 노치에 대한 인셋을 적용합니다.
 *
 * @param view 인셋을 적용할 View. 지정하지 않으면 android.R.id.content를 사용합니다.
 */
fun Activity.applyEdgeToEdgeInsets(view: View? = null) {
    val targetView = view ?: findViewById(android.R.id.content)
    ViewCompat.setOnApplyWindowInsetsListener(targetView) { rootView, windowInsets ->
        val insets = windowInsets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout(),
        )
        rootView.updatePadding(
            left = insets.left,
            top = insets.top,
            right = insets.right,
            bottom = insets.bottom,
        )
        WindowInsetsCompat.CONSUMED
    }
}
