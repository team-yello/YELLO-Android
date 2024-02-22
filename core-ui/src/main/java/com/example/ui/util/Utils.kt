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

    fun setChosungText(name: String, number: Int): String {
        val firstChosung = name[number]
        val chosungUnicode = Character.UnicodeBlock.of(firstChosung.toInt())
        return if (chosungUnicode == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO ||
            chosungUnicode == Character.UnicodeBlock.HANGUL_JAMO ||
            chosungUnicode == Character.UnicodeBlock.HANGUL_SYLLABLES
        ) {
            val chosungIndex = (firstChosung.toInt() - 0xAC00) / 28 / 21
            val chosung = Character.toChars(chosungIndex + 0x1100)[0]
            chosung.toString() // 출력: "ㄱ"
        } else {
            ""
        }
    }
}