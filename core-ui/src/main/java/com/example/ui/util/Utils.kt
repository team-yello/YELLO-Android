package com.example.ui.util

import android.content.Context
import android.content.Intent

object Utils {
    fun restartApp(context: Context, toastMsg: String) {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val componentName = packageManager.getLaunchIntentForPackage(packageName)?.component
        Intent.makeRestartActivityTask(componentName).apply {
            putExtra("TOAST_MSG", toastMsg)
            context.startActivity(this)
        }
        Runtime.getRuntime().exit(0)
    }
}