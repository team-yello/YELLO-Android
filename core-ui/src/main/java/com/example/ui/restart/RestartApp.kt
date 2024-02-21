package com.example.ui.restart

import android.content.Context
import android.content.Intent

// TODO : 패키지 및 파일명 더 나은 거 없으려나
fun restartApp(context: Context, toastMsg: String?) {
    val packageManager = context.packageManager
    val packageName = context.packageName
    val componentName = packageManager.getLaunchIntentForPackage(packageName)?.component
    Intent.makeRestartActivityTask(componentName).apply {
        if (toastMsg != null) putExtra("TOAST_MSG", toastMsg)
        context.startActivity(this)
    }
    Runtime.getRuntime().exit(0)
}