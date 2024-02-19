package com.example.data.util

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.TimeZone

fun String.toRemainTime(totalTime: Long): Long {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        val currentTime = System.currentTimeMillis()
        val targetDateTime = dateFormat.parse(this)?.time ?: return 0
        val timeDifference = targetDateTime - currentTime

        Timber.d("REMAINING TIME : ${(timeDifference / 1000) + totalTime}")

        (timeDifference / 1000) + totalTime
    } catch (e: ParseException) {
        e.printStackTrace()
        totalTime
    }
}