package com.example.data.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FileParser @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun execute(fileName: String): String {
        return context.assets
            .open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }
}