package com.el.yello.presentation.tutorial.contract

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.el.yello.R

@Immutable
data class TutorialEndState(
    val isPlus: Boolean = false
) {
    @DrawableRes
    fun getTutorialEndImage(): Int {
        return if (isPlus) {
            R.drawable.img_tutorial_plus_point
        } else {
            R.drawable.img_tutorial_point
        }
    }
}

sealed interface TutorialEndSideEffect {
    data object NavigateToMainActivity : TutorialEndSideEffect
}
