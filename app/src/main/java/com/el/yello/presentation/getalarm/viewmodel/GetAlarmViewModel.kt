package com.el.yello.presentation.getalarm.viewmodel

import androidx.lifecycle.ViewModel
import com.el.yello.presentation.getalarm.contract.GetAlarmSideEffect
import com.el.yello.presentation.getalarm.contract.GetAlarmState
import com.el.yello.util.manager.AmplitudeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GetAlarmViewModel @Inject constructor()
    : ContainerHost<GetAlarmState, GetAlarmSideEffect>, ViewModel() {
    override val container: Container<GetAlarmState, GetAlarmSideEffect> = container(GetAlarmState())

    fun initGetAlarm(isFromOnBoarding: Boolean, isCodeTextEmpty: Boolean) = intent {
        reduce {
            state.copy(
                isFromOnBoarding = isFromOnBoarding,
                isCodeTextEmpty = isCodeTextEmpty
            )
        }
    }

    fun onPermissionResult(isGranted: Boolean) = intent {
        if (isGranted) {
            AmplitudeManager.updateUserProperties(EVENT_PUSH_NOTIFICATION, VALUE_ENABLED)
        } else {
            AmplitudeManager.updateUserProperties(EVENT_PUSH_NOTIFICATION, VALUE_DISABLED)
        }
        postSideEffect(GetAlarmSideEffect.NavigateToTutorial)
    }

    fun onStartYelloClickEvent() = intent {
        AmplitudeManager.trackEventWithProperties(EVENT_CLICK_ONBOARDING_NOTIFICATION)
    }

    companion object {
        private const val EVENT_PUSH_NOTIFICATION = "user_pushnotification"
        private const val VALUE_ENABLED = "enabled"
        private const val VALUE_DISABLED = "disabled"
        private const val EVENT_CLICK_ONBOARDING_NOTIFICATION = "click_onboarding_notification"
    }
}
