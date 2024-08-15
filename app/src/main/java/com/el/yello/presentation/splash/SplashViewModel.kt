package com.el.yello.presentation.splash

import androidx.lifecycle.ViewModel
import com.el.yello.BuildConfig.VERSION_NAME
import com.example.domain.repository.AuthRepository
import com.example.ui.state.UiState
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseDatabase: DatabaseReference,
) : ViewModel() {
    private val _isLatestVersion = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val isLatestVersion get() = _isLatestVersion.asStateFlow()

    fun getIsAutoLogin(): Boolean = authRepository.getAutoLogin()

    fun checkLatestUpdate() {
        firebaseDatabase.child(PATH_REALTIME_VERSION).get()
            .addOnSuccessListener { snapshot ->
                val updateVersion = snapshot.value.toString().toFloat()
                val currentVersion = VERSION_NAME.toFloat()

                val isLatestVersion = currentVersion >= updateVersion
                _isLatestVersion.value = UiState.Success(isLatestVersion)
            }
            .addOnFailureListener { e ->
                _isLatestVersion.value = UiState.Failure(e.toString())
            }
    }

    companion object {
        private const val PATH_REALTIME_VERSION = "version"
    }
}
