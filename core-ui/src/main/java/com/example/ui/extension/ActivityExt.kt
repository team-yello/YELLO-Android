package com.example.ui.extension

import android.app.Activity
import android.content.Intent

inline fun <reified T : Activity> Activity.navigateTo() {
    Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(this)
    }
}