package com.el.yello.presentation.getalarm.contract

import androidx.compose.runtime.Immutable

@Immutable
data class GetAlarmState(
    val isLoading: Boolean = false,
    val isFromOnBoarding: Boolean = false,
    val isCodeTextEmpty: Boolean = false
)

sealed interface GetAlarmSideEffect {
    data object NavigateToTutorial : GetAlarmSideEffect
}
