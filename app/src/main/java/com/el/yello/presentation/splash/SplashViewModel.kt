package com.el.yello.presentation.splash

import androidx.lifecycle.ViewModel
import com.example.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun getIsAutoLogin(): Boolean = authRepository.getAutoLogin()
}
