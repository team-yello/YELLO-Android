package com.el.yello.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import com.el.yello.presentation.onboarding.contract.EditNameSideEffect
import com.el.yello.presentation.onboarding.contract.EditNameState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class EditNameViewModel @Inject constructor() : ContainerHost<EditNameState, EditNameSideEffect>, ViewModel() {

    override val container = container<EditNameState, EditNameSideEffect>(EditNameState())

    fun initUserData(
        kakaoId: Long,
        name: String,
        profileImg: String,
        email: String,
        gender: String,
    ) = intent {

        reduce {
            state.copy(
                kakaoId = kakaoId,
                nameText = name,
                isValidName = checkName(name),
                checkNameLength = name.trim().length >= 2,
                profileImg = profileImg,
                email = email,
                gender = gender,
            )
        }
    }

    private fun checkName(name: String?): Boolean {
        if (name.isNullOrBlank()) return false
        return Pattern.matches(REGEX_NAME_PATTERN, name)
    }

    fun onNameTextChanged(name: String) = intent {
        val limitedText = if (name.length > 4) name.take(4) else name
        reduce {
            state.copy(
                nameText = limitedText,
                isValidName = checkName(limitedText),
                checkNameLength = limitedText.trim().length >= 2
            )
        }
    }

    fun onDeleteClick() = intent {
        reduce {
            state.copy(
                nameText = "",
                isValidName = false,
                checkNameLength = false
            )
        }
    }

    fun onConfirmClick() = intent {
        if (state.isConfirmButtonEnabled) {
            postSideEffect(
                EditNameSideEffect.NavigateToOnBoarding(
                    kakaoId = state.kakaoId,
                    name = state.nameText,
                    profileImage = state.profileImg,
                    email = state.email,
                    gender = state.gender,
                )
            )
        }
    }

    companion object {
        private const val REGEX_NAME_PATTERN = "^([가-힣]*)\$"
    }
}
