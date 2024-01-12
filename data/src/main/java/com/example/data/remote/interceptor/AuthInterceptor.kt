package com.example.data.remote.interceptor

import com.example.data.model.response.BaseResponse
import com.example.data.model.response.onboarding.ResponseAuthToken
import com.example.domain.YelloDataStore
import com.yello.data.BuildConfig.BASE_URL
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val json: Json,
    private val dataStore: YelloDataStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Timber.d("ACCESS TOKEN : ${dataStore.userToken}")
        val originalRequest = chain.request()
        val authRequest = if (dataStore.isLogin) {
            originalRequest.newAuthBuilder().build()
        } else {
            originalRequest
        }
        val response = chain.proceed(authRequest)

        when (response.code) {
            CODE_TOKEN_EXPIRED -> {
                try {
                    val refreshTokenRequest = originalRequest.newBuilder().post("".toRequestBody())
                        .url("$BASE_URL/api/v1/auth/token/issue")
                        .addHeader(HEADER_ACCESS_TOKEN, "Bearer ${dataStore.userToken}")
                        .addHeader(HEADER_REFRESH_TOKEN, "Bearer ${dataStore.refreshToken}")
                        .build()
                    val refreshTokenResponse = chain.proceed(refreshTokenRequest)
                    Timber.d("GET REFRESH TOKEN RESPONSE : $refreshTokenResponse")

                    if (refreshTokenResponse.isSuccessful) {
                        val responseToken = json.decodeFromString(
                            refreshTokenResponse.body?.string().toString(),
                        ) as BaseResponse<ResponseAuthToken>

                        with(dataStore) {
                            userToken = responseToken.data?.accessToken ?: ""
                            refreshToken = responseToken.data?.refreshToken ?: ""
                        }
                        refreshTokenResponse.close()
                        val newRequest = originalRequest.newAuthBuilder().build()
                        return chain.proceed(newRequest)
                    }

                    with(dataStore) {
                        isLogin = false
                        userToken = ""
                        refreshToken = ""
                    }
                } catch (t: Throwable) {
                    Timber.e(t)
                    with(dataStore) {
                        isLogin = false
                        dataStore.userToken = ""
                        dataStore.refreshToken = ""
                    }
                }
            }
        }
        return response
    }

    private fun Request.newAuthBuilder() =
        this.newBuilder().addHeader(HEADER_AUTHORIZATION, "Bearer ${dataStore.userToken}")

    companion object {
        private const val CODE_TOKEN_EXPIRED = 401
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val HEADER_ACCESS_TOKEN = "X-ACCESS-AUTH"
        private const val HEADER_REFRESH_TOKEN = "X-REFRESH-AUTH"
    }
}
