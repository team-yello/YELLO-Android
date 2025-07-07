package com.el.yello.presentation.tutorial

import androidx.lifecycle.ViewModel
import com.el.yello.util.manager.AmplitudeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor() : ContainerHost<TutorialState, TutorialSideEffect>,
    ViewModel() {

    override val container = container<TutorialState, TutorialSideEffect>(TutorialState())

    fun initTutorial(isCodeTextEmpty: Boolean, isFromOnBoarding: Boolean) = intent {
        reduce {
            state.copy(
                isCodeTextEmpty = isCodeTextEmpty,
                isFromOnBoarding = isFromOnBoarding
            )
        }
        trackTutorialScreen(0)
    }

    fun onScreenClick() = intent {
        if (state.isLastScreen()) {
            handleLastScreen(state)
        } else {
            val nextStep = state.currentScreen + 1
            reduce { state.copy(currentScreen = nextStep) }
            trackTutorialScreen(nextStep)
        }
    }

    private fun handleLastScreen(state: TutorialState) = intent {
        val sideEffect = if (state.isFromOnBoarding) {
            if (state.isCodeTextEmpty) {
                TutorialSideEffect.NavigateToTutorialEnd
            } else {
                TutorialSideEffect.NavigateToTutorialEndPlus
            }
        } else {
            TutorialSideEffect.NavigateToMainActivity
        }
        postSideEffect(sideEffect)
    }

    private fun trackTutorialScreen(step: Int) {
        val stepValue = when (step) {
            0 -> "1"
            1 -> "2"
            2 -> "3"
            3 -> "4"
            else -> "1"
        }

        AmplitudeManager.trackEventWithProperties(
            EVENT_VIEW_ONBOARDING_TUTORIAL,
            JSONObject().put(NAME_TUTORIAL_STEP, stepValue),
        )
    }

    companion object {
        private const val EVENT_VIEW_ONBOARDING_TUTORIAL = "view_onboarding_tutorial"
        private const val NAME_TUTORIAL_STEP = "tutorial_step"
    }
}
