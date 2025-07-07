package com.el.yello.presentation.tutorial

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.el.yello.R

@Immutable
data class TutorialState(
    val currentScreen: Int = 0,
    val isCodeTextEmpty: Boolean = false,
    val isFromOnBoarding: Boolean = false
) {
    @DrawableRes
    fun getCurrentTutorialImage(): Int {
        return when (currentScreen) {
            0 -> R.drawable.img_tutorial_a
            1 -> R.drawable.img_tutorial_b
            2 -> R.drawable.img_tutorial_c
            3 -> R.drawable.img_tutorial_d
            else -> R.drawable.img_tutorial_a
        }
    }

    fun isLastScreen(): Boolean = currentScreen == 3
}

sealed interface TutorialSideEffect {
    data object NavigateToTutorialEnd : TutorialSideEffect
    data object NavigateToTutorialEndPlus : TutorialSideEffect
    data object NavigateToMainActivity : TutorialSideEffect
}
