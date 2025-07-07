package com.el.yello.presentation.setting

data class ProfileQuitTwoState(
    val isLoading: Boolean = false
)

sealed interface ProfileQuitTwoSideEffect {
    data object NavigateBack : ProfileQuitTwoSideEffect
    data object NavigateToProfileQuitReason : ProfileQuitTwoSideEffect
}