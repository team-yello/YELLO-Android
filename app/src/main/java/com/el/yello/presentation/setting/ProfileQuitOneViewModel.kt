package com.el.yello.presentation.setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ProfileQuitOneViewModel @Inject constructor() :
    ContainerHost<ProfileQuitOneState, ProfileQuitOneSideEffect>, ViewModel() {
    override val container =
        container<ProfileQuitOneState, ProfileQuitOneSideEffect>(ProfileQuitOneState())

    fun onBackClick() = intent {
        postSideEffect(ProfileQuitOneSideEffect.NavigateBack)
    }

    fun onQuitClick() = intent {
        postSideEffect(ProfileQuitOneSideEffect.NavigateToProfileQuitTwo)
    }
}
