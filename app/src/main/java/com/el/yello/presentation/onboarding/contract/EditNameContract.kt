package com.el.yello.presentation.onboarding.contract

import androidx.compose.runtime.Immutable

@Immutable
data class EditNameState(
    val kakaoId: Long = 0L,
    val nameText: String = "",
    val isValidName: Boolean = false,
    val checkNameLength: Boolean = false,
    val profileImg: String = "",
    val email: String = "",
    val gender: String = ""
) {
    val isConfirmButtonEnabled: Boolean
        get() = isValidName && nameText.isNotEmpty() && checkNameLength
}

sealed interface EditNameSideEffect {
    data class NavigateToOnBoarding(
        val kakaoId: Long,
        val name: String,
        val profileImage: String,
        val email: String,
        val gender: String,
    ) : EditNameSideEffect

    data class ShowSnackBar(val message: String) : EditNameSideEffect
}
