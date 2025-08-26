package com.el.yello.presentation.onboarding.contract

import androidx.compose.runtime.Immutable
import com.example.domain.enum.StudentType

@Immutable
data class SelectStudentState(
    val selectedStudentType: StudentType? = null
)

sealed interface SelectStudentSideEffect {
    data object NavigateToHighSchool : SelectStudentSideEffect
    data object NavigateToUniversity : SelectStudentSideEffect
}
