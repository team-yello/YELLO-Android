package com.example.data.remote.interceptor

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
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
    @ApplicationContext private val context: Context,
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

        when (response.code()) {
            CODE_TOKEN_EXPIRED -> {
                try {
                    val refreshTokenRequest = originalRequest.newBuilder().get()
                        .url("$BASE_URL/api/v1/auth/token")
                        .addHeader(HEADER_ACCESS_TOKEN, dataStore.userToken)
                        .addHeader(HEADER_REFRESH_TOKEN, dataStore.refreshToken)
                        .build()
                    val refreshTokenResponse = chain.proceed(refreshTokenRequest)
                    Timber.d("GET REFRESH TOKEN : $refreshTokenResponse")

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
                        isLogin = false
                        userToken = ""
                        refreshToken = ""
                    }
                    startActivity(
                        context,
                        Intent.makeRestartActivityTask(
                            context.packageManager.getLaunchIntentForPackage(
                                context.packageName,
                            )?.component,
                        ),
                        null,
                    )
                } catch (t: Throwable) {
                    Timber.e(t)
                    with(dataStore) {
                        isLogin = false
                        dataStore.userToken = ""
                        dataStore.refreshToken = ""
                    }
                    startActivity(
                        context,
                        Intent.makeRestartActivityTask(
                            context.packageManager.getLaunchIntentForPackage(
                                context.packageName,
                            )?.component,
                        ),
                        null,
                    )
                }
            }
        }
        return response
    }

    private fun Request.newAuthBuilder() =
        this.newBuilder().addHeader(HEADER_AUTHORIZATION, "Bearer ${dataStore.userToken}")

    private fun navigateToMainScreen() {
    }

    companion object {
        private const val CODE_TOKEN_EXPIRED = 401
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val HEADER_ACCESS_TOKEN = "accessToken"
        private const val HEADER_REFRESH_TOKEN = "refreshToken"
    }
}
