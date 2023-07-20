package com.example.domain.repository

interface AuthRepository {
    fun setAutoLogin(userToken: String, refreshToken: String)
    fun getAutoLogin(): Boolean
    fun setYelloId(yelloId: String)
    fun getYelloId(): String?
    fun clearLocalPref()
}