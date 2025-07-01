package com.el.yello.presentation.setting

import androidx.lifecycle.ViewModel
import com.el.yello.presentation.setting.SettingActivity.Companion.CLICK_PROFILE_LOGOUT
import com.el.yello.util.manager.AmplitudeManager
import com.example.domain.repository.AuthRepository
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.suspendCancellableCoroutine
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ContainerHost<SettingState, SettingSideEffect>, ViewModel() {
    override val container = container<SettingState, SettingSideEffect>(SettingState())

    private suspend fun logoutSuspending(): String = suspendCancellableCoroutine { continuation ->
        UserApiClient.instance.logout { error ->
            if (error == null) {
                authRepository.clearLocalPref()
                continuation.resume("success")
            } else {
                continuation.resume(error.message.toString())
            }
        }
    }

    fun logout() = intent {
        AmplitudeManager.trackEventWithProperties(CLICK_PROFILE_LOGOUT)

        reduce { state.copy(isLoading = true) }
        val logoutMsg = logoutSuspending()
        if (logoutMsg == "success") {
            postSideEffect(SettingSideEffect.SuccessLogout)
        } else {
            postSideEffect(SettingSideEffect.FailureLogout(logoutMsg))
        }
        reduce { state.copy(isLoading = false) }
    }

    fun onClickBack() = intent {
        postSideEffect(SettingSideEffect.NavigateBack)
    }

    fun onCustomerSupportClick() = intent {
        postSideEffect(SettingSideEffect.NavigateCustomerSupport)
    }

    fun onClickPrivacyPolicyClick() = intent {
        postSideEffect(SettingSideEffect.NavigatePrivacyPolicy)
    }

    fun onClickTermsOfServiceClick() = intent {
        postSideEffect(SettingSideEffect.NavigateTermsOfService)
    }

    fun onAccountDeletionClick() = intent {
        postSideEffect(SettingSideEffect.NavigateAccountDeletion)
    }
}