package com.example.data.remote.interceptor

import android.content.Context
import com.example.data.model.response.onboarding.ResponseAuthToken
import com.example.domain.YelloDataStore
import com.yello.data.BuildConfig.BASE_URL
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val json: Json,
    private val dataStore: YelloDataStore,
    @ApplicationContext context: Context,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authRequest = if (dataStore.isLogin) {
            originalRequest.newAuthBuilder().build()
        } else {
            //나중에 없애기
            originalRequest.newAuthBuilder().build()
        }
        val response = chain.proceed(authRequest)

        when (response.code()) {
            CODE_TOKEN_EXPIRED -> {
                try {
                    val refreshTokenRequest = originalRequest.newBuilder().get()
                        .url("${BASE_URL}/api/v1/auth/token")
                        .addHeader(HEADER_ACCESS_TOKEN, dataStore.userToken)
                        .addHeader(HEADER_REFRESH_TOKEN, dataStore.refreshToken)
                        .build()
                    val refreshTokenResponse = chain.proceed(refreshTokenRequest)

                    if (refreshTokenResponse.isSuccessful) {
                        val responseToken: ResponseAuthToken =
                            json.decodeFromString(
                                refreshTokenResponse.body()?.string()
                                    ?: throw IllegalStateException("refreshTokenResponse is null $refreshTokenResponse"),
                            )

                        with(dataStore) {
                            userToken = responseToken.accessToken
                            refreshToken = responseToken.refreshToken
                        }
                        refreshTokenResponse.close()
                        val newRequest = originalRequest.newAuthBuilder().build()
                        return chain.proceed(newRequest)
                    }

                    with(dataStore) {
                        userToken = ""
                        refreshToken = ""
                        // TODO: 앱 재시작
                    }
                } catch (t: Throwable) {
                    Timber.e(t)
                    dataStore.userToken = ""
                    dataStore.refreshToken = ""
                    // TODO: 앱 재시작
                }
            }
        }
        return response
    }

    private fun Request.newAuthBuilder() =
        this.newBuilder().addHeader(HEADER_ACCESS_TOKEN, dataStore.userToken)
    //datastore.usertoken 대신 eyJ0eXBlIjoiYWNjZXNzVG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyOTAyMTQ3MTY5IiwianRpIjoiMTYxIiwiaWF0IjoxNjg5NTMzMjgyLCJleHAiOjE2ODk2MTk2ODJ9.yzO71BRbZLoitkr0iv6R2JYEjp-e2RMUZVQHMm81RDI

    companion object {
        private const val CODE_TOKEN_EXPIRED = 401

        private const val HEADER_ACCESS_TOKEN = "Authorization"
        private const val HEADER_REFRESH_TOKEN = "refreshToken"
    }
}
