package com.example.domain

interface YelloDataStore {
    var userToken: String
    var refreshToken: String
    var deviceToken: String
    var isLogin: Boolean
    var yelloId: String
    var isFirstLogin: Boolean

    fun clearLocalPref()
}
