package com.el.yello.presentation.main.profile.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.ProfileRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnivProfileDetailViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _getUserDataState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val getUserDataState: StateFlow<UiState<String>> = _getUserDataState

    val name = MutableLiveData("")
    val id = MutableLiveData("")
    val school = MutableLiveData("")
    val subGroup = MutableLiveData("")
    val admYear = MutableLiveData(0)

    fun getUserDataFromServer() {
        viewModelScope.launch {
            profileRepository.getUserData()
                .onSuccess { profile ->
                    if (profile == null) {
                        _getUserDataState.value = UiState.Failure(toString())
                        return@launch
                    }
                    name.value = profile.name
                    id.value = profile.yelloId
                    school.value = profile.groupName
                    subGroup.value = profile.subGroupName
                    admYear.value = profile.groupAdmissionYear
                    _getUserDataState.value = UiState.Success(profile.profileImageUrl)
                }
                .onFailure {
                    _getUserDataState.value = UiState.Failure(it.message.toString())
                }
        }
    }
}