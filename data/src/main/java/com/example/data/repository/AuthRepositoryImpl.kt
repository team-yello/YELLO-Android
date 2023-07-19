package com.example.data.repository

import android.content.SharedPreferences
import com.example.domain.YelloDataStore
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataStore: SharedPreferences,
    private val yelloDataStore: YelloDataStore,
) : AuthRepository {
    override fun setAutoLogin(userToken: String, refreshToken: String) {
        yelloDataStore.isLogin = true
        yelloDataStore.userToken = userToken
        yelloDataStore.refreshToken = refreshToken
    }

    override fun getAutoLogin(): Boolean = yelloDataStore.isLogin

    override fun setYelloId(yelloId: String) {
        yelloDataStore.yelloId = yelloId
    }

    override fun getYelloId(): String? = yelloDataStore.yelloId

    override fun clearLocalPref() = yelloDataStore.clearLocalPref()
}
