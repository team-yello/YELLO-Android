package com.el.yello.presentation.setting

import androidx.lifecycle.ViewModel
import com.el.yello.util.manager.AmplitudeManager
import com.example.domain.entity.ProfileQuitReasonModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.ProfileRepository
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class ProfileReasonViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
) : ContainerHost<ProfileReasonState, ProfileReasonSideEffect>, ViewModel() {
    override val container =
        container<ProfileReasonState, ProfileReasonSideEffect>(ProfileReasonState())

    fun updateEtcText(text: String) = intent {
        reduce {
            state.copy(etcText = text, isEnableButton = text.isNotEmpty())
        }
    }

    fun selectReason(index: Int) = intent {
        reduce {
            state.copy(selectedReasonIndex = index, isEnableButton = index != 7 || (state.etcText.isNotEmpty()))
        }
    }

    fun onDismissProfileQuitDialog() = intent {
        reduce { state.copy(isShowProfileQuitDialog = false) }
    }

    fun onDismissProfileQuitInviteFriendDialog() = intent {
        reduce {
            state.copy(
                isShowProfileQuitInviteFriendDialog = false,
                isShowProfileQuitDialog = true
            )
        }
    }

    fun navigateToMainActivity() = intent {
        postSideEffect(ProfileReasonSideEffect.NavigateToMainActivity)
    }

    fun deleteUserDataToServer() = intent {
        AmplitudeManager.trackEventWithProperties(
            EVENT_CLICK_PROFILE_WITHDRAWAL,
            JSONObject().put(NAME_WITHDRAWAL_BUTTON, VALUE_WITHDRAWAL_FOUR),
        )

        reduce { state.copy(isLoading = true) }

        val quitReason = if (state.selectedReasonIndex == 7) {
            state.etcText
        } else {
            state.reasonList[state.selectedReasonIndex]
        }

        profileRepository.deleteUserData(ProfileQuitReasonModel(quitReason))
            .onSuccess {
                clearLocalInfo()
                delay(300)
                quitKaKaoAccount()
            }
            .onFailure {
                postSideEffect(ProfileReasonSideEffect.ShowToast(it.message.toString()))
            }
    }

    private fun quitKaKaoAccount() = intent {
        val logoutMsg = unlinkSuspending()
        if (logoutMsg == "success") {
            postSideEffect(ProfileReasonSideEffect.SuccessKaKaoUnlink)
        } else {
            postSideEffect(ProfileReasonSideEffect.ShowToast(logoutMsg))
        }
    }

    private suspend fun unlinkSuspending(): String = suspendCancellableCoroutine { continuation ->
        UserApiClient.instance.unlink { error ->
            if (error == null) {
                continuation.resume("success")
            } else {
                continuation.resume(error.message.toString())
            }
        }
    }

    fun onBackClick() = intent {
        postSideEffect(ProfileReasonSideEffect.NavigateBack)
    }

    fun onCompleteClick() = intent {
        if (state.selectedReasonIndex == 0) {
            reduce { state.copy(isShowProfileQuitInviteFriendDialog = true) }
        } else {
            reduce { state.copy(isShowProfileQuitDialog = true) }
        }
    }

    private fun clearLocalInfo() {
        authRepository.clearLocalPref()
    }

    companion object {
        private const val EVENT_CLICK_PROFILE_WITHDRAWAL = "click_profile_withdrawal"
        private const val NAME_WITHDRAWAL_BUTTON = "withdrawal_button"
        private const val VALUE_WITHDRAWAL_FOUR = "withdrawal4"
    }
}