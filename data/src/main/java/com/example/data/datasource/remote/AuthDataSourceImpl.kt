package com.example.data.datasource.remote

import com.example.data.datasource.AuthDataSource
import com.example.data.model.request.auth.toDeviceToken
import com.example.data.remote.service.AuthService
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val service: AuthService
): AuthDataSource {
    override suspend fun putDeviceToken(token: String): Boolean {
        return runCatching { service.putDeviceToken(token.toDeviceToken()) }.isSuccess
    }
}