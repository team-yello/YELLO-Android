package com.example.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.domain.YelloDataStore
import javax.inject.Inject

class YelloDataStoreImpl @Inject constructor(
    private val dataStore: SharedPreferences,
) : YelloDataStore {
    override var userToken: String
        get() = dataStore.getString(PREF_USER_TOKEN, "") ?: ""
        set(value) = dataStore.edit { putString(PREF_USER_TOKEN, value) }

    override var refreshToken: String
        get() = dataStore.getString(PREF_REFRESH_TOKEN, "") ?: ""
        set(value) = dataStore.edit { putString(PREF_REFRESH_TOKEN, value) }

    override var isLogin: Boolean
        get() = dataStore.getBoolean(PREF_IS_LOGIN, false)
        set(value) = dataStore.edit { putBoolean(PREF_IS_LOGIN, value) }

    override var yelloId: String
        get() = dataStore.getString(PREF_YELLO_ID, "") ?: ""
        set(value) = dataStore.edit { putString(PREF_YELLO_ID, value) }

    override fun clearLocalPref() = dataStore.edit { clear() }

    companion object {
        private const val PREF_USER_TOKEN = "USER_TOKEN"
        private const val PREF_REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val PREF_IS_LOGIN = "IS_LOGIN"
        private const val PREF_YELLO_ID = "YELLO_ID"
    }
}
