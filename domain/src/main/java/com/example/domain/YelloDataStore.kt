package com.example.domain

interface YelloDataStore {
    var userToken: String
    var refreshToken: String
    var isLogin: Boolean
    var yelloId: String
    fun clearLocalPref()
}
