package com.el.yello.presentation.setting

import androidx.lifecycle.ViewModel
import com.el.yello.util.manager.AmplitudeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ProfileQuitTwoViewModel @Inject constructor() : ViewModel(),
    ContainerHost<ProfileQuitTwoState, ProfileQuitTwoSideEffect> {

    override val container = container<ProfileQuitTwoState, ProfileQuitTwoSideEffect>(ProfileQuitTwoState())

    fun onBackClick() = intent {
        postSideEffect(ProfileQuitTwoSideEffect.NavigateBack)
    }

    fun onQuitClick() = intent {
        AmplitudeManager.trackEventWithProperties(
            EVENT_CLICK_PROFILE_WITHDRAWAL,
            JSONObject().put(NAME_WITHDRAWAL_BUTTON, VALUE_WITHDRAWAL_THREE),
        )
        postSideEffect(ProfileQuitTwoSideEffect.NavigateToProfileQuitReason)
    }

    companion object {
        private const val EVENT_CLICK_PROFILE_WITHDRAWAL = "click_profile_withdrawal"
        private const val NAME_WITHDRAWAL_BUTTON = "withdrawal_button"
        private const val VALUE_WITHDRAWAL_THREE = "withdrawal3"
    }
}
