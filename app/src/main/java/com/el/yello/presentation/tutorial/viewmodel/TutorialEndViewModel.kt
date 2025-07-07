package com.el.yello.presentation.tutorial.viewmodel

import androidx.lifecycle.ViewModel
import com.el.yello.presentation.tutorial.contract.TutorialEndSideEffect
import com.el.yello.presentation.tutorial.contract.TutorialEndState
import com.el.yello.util.manager.AmplitudeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TutorialEndViewModel @Inject constructor() : ContainerHost<TutorialEndState, TutorialEndSideEffect>, ViewModel() {

    override val container = container<TutorialEndState, TutorialEndSideEffect>(TutorialEndState())

    fun initTutorialEnd(isPlus: Boolean) = intent {
        reduce { state.copy(isPlus = isPlus) }
    }

    fun onEndButtonClick() = intent {
        AmplitudeManager.trackEventWithProperties(EVENT_CLICK_ONBOARDING_YELLO_START)
        postSideEffect(TutorialEndSideEffect.NavigateToMainActivity)
    }

    companion object {
        private const val EVENT_CLICK_ONBOARDING_YELLO_START = "click_onboarding_yellostart"
    }
}
