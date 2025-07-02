package com.el.yello.presentation.setting

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileQuitOneState(
    val isLoading: Boolean = false
)

sealed class ProfileQuitOneSideEffect {
    data object NavigateBack : ProfileQuitOneSideEffect()
    data object NavigateToProfileQuitTwo : ProfileQuitOneSideEffect()
}
