package com.example.data.datasource

interface AuthDataSource {
    suspend fun putDeviceToken(token: String): Boolean
}
