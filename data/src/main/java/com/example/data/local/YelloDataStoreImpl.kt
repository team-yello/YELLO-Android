package com.example.data.local

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.domain.YelloDataStore
import com.yello.data.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class YelloDataStoreImpl @Inject constructor(
    @ApplicationContext context: Context,
) : YelloDataStore {
    private val userDelegate by lazy {
        if (BuildConfig.DEBUG) {
            context.getSharedPreferences(
                context.packageName,
                Context.MODE_PRIVATE,
            )
        } else {
            EncryptedSharedPreferences.create(
                context.packageName,
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
        }
    }

    private val appDelegate by lazy {
        if (BuildConfig.DEBUG) {
            context.getSharedPreferences(
                "APP_DATA",
                Context.MODE_PRIVATE,
            )
        } else {
            EncryptedSharedPreferences.create(
                "APP_DATA",
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
        }
    }

    override var userToken: String
        get() = userDelegate.getString(PREF_USER_TOKEN, "") ?: ""
        set(value) = userDelegate.edit { putString(PREF_USER_TOKEN, value) }

    override var refreshToken: String
        get() = userDelegate.getString(PREF_REFRESH_TOKEN, "") ?: ""
        set(value) = userDelegate.edit { putString(PREF_REFRESH_TOKEN, value) }

    override var deviceToken: String
        get() = userDelegate.getString(PREF_DEVICE_TOKEN, "") ?: ""
        set(value) = userDelegate.edit { putString(PREF_DEVICE_TOKEN, value) }

    override var isLogin: Boolean
        get() = userDelegate.getBoolean(PREF_IS_LOGIN, false)
        set(value) = userDelegate.edit { putBoolean(PREF_IS_LOGIN, value) }

    override var yelloId: String
        get() = userDelegate.getString(PREF_YELLO_ID, "") ?: ""
        set(value) = userDelegate.edit { putString(PREF_YELLO_ID, value) }
    override var isFirstLogin: Boolean
        get() = appDelegate.getBoolean(PREF_IS_FIRST_LOGIN, false)
        set(value) = appDelegate.edit { putBoolean(PREF_IS_FIRST_LOGIN, value) }

    override fun clearLocalPref() = userDelegate.edit { clear() }

    companion object {
        private const val PREF_USER_TOKEN = "USER_TOKEN"
        private const val PREF_REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val PREF_DEVICE_TOKEN = "DEVICE_TOKEN"
        private const val PREF_IS_LOGIN = "IS_LOGIN"
        private const val PREF_YELLO_ID = "YELLO_ID"
        private const val PREF_IS_FIRST_LOGIN = "IS_FIRST_LOGIN"
    }
}
