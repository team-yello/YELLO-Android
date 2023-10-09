package com.example.domain.repository

interface AuthRepository {
    fun setAutoLogin(userToken: String, refreshToken: String)
    fun getAutoLogin(): Boolean
    fun setYelloId(yelloId: String)
    fun getYelloId(): String
    fun setIsFirstLoginData()
    fun getIsFirstLoginData(): Boolean
    fun setDeviceToken(deviceToken: String)
    fun getDeviceToken(): String
    suspend fun putDeviceToken(token: String): Boolean
    fun clearLocalPref()
}
