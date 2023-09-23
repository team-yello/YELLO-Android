package com.example.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.data.local.qualifier.App
import com.example.data.local.qualifier.User
import com.example.domain.YelloDataStore
import com.example.domain.entity.vote.StoredVote
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import javax.inject.Inject

class YelloDataStoreImpl @Inject constructor(
    @User private val userPref: SharedPreferences,
    @App private val appPref: SharedPreferences
) : YelloDataStore {
    override var userToken: String
        get() = userPref.getString(PREF_USER_TOKEN, "") ?: ""
        set(value) = userPref.edit { putString(PREF_USER_TOKEN, value) }

    override var refreshToken: String
        get() = userPref.getString(PREF_REFRESH_TOKEN, "") ?: ""
        set(value) = userPref.edit { putString(PREF_REFRESH_TOKEN, value) }

    override var deviceToken: String
        get() = userPref.getString(PREF_DEVICE_TOKEN, "") ?: ""
        set(value) = userPref.edit { putString(PREF_DEVICE_TOKEN, value) }

    override var isLogin: Boolean
        get() = userPref.getBoolean(PREF_IS_LOGIN, false)
        set(value) = userPref.edit { putBoolean(PREF_IS_LOGIN, value) }

    override var yelloId: String
        get() = userPref.getString(PREF_YELLO_ID, "") ?: ""
        set(value) = userPref.edit { putString(PREF_YELLO_ID, value) }

    override var isFirstLogin: Boolean
        get() = appPref.getBoolean(PREF_IS_FIRST_LOGIN, false)
        set(value) = appPref.edit { putBoolean(PREF_IS_FIRST_LOGIN, value) }

    override var storedVote: StoredVote?
        get() {
            val vote = userPref.getString(PREF_STORED_VOTE, "")
            return try {
                Gson().fromJson(vote, StoredVote::class.java)
            } catch (e: Exception) {
                null
            }
        }
        set(value) = userPref.edit {
            val storedVote = GsonBuilder().create().toJson(value)
            putString(PREF_STORED_VOTE, storedVote)
        }

    override fun clearLocalPref() = userPref.edit { clear() }

    companion object {
        private const val PREF_USER_TOKEN = "USER_TOKEN"
        private const val PREF_REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val PREF_DEVICE_TOKEN = "DEVICE_TOKEN"
        private const val PREF_IS_LOGIN = "IS_LOGIN"
        private const val PREF_YELLO_ID = "YELLO_ID"
        private const val PREF_IS_FIRST_LOGIN = "IS_FIRST_LOGIN"
        private const val PREF_STORED_VOTE = "STORED_VOTE"
    }
}
