package com.el.yello.presentation.setting

import androidx.compose.runtime.Immutable

@Immutable
data class SettingState(
    val isLoading: Boolean = false
)

sealed class SettingSideEffect {
    data object SuccessLogout : SettingSideEffect()
    data class FailureLogout(val msg: String) : SettingSideEffect()
    data object NavigateBack : SettingSideEffect()
    data object NavigateCustomerSupport : SettingSideEffect()
    data object NavigatePrivacyPolicy : SettingSideEffect()
    data object NavigateTermsOfService : SettingSideEffect()
    data object NavigateAccountDeletion : SettingSideEffect()
}