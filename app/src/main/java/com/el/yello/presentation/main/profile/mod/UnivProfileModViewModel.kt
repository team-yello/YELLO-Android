package com.el.yello.presentation.main.profile.mod

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.ProfileModRequestModel
import com.example.domain.repository.ProfileRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnivProfileModViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _getUserDataResult = MutableSharedFlow<Boolean>()
    val getUserDataResult: SharedFlow<Boolean> = _getUserDataResult

    val lastModDate = MutableLiveData("")
    val school = MutableLiveData("")
    val subGroup = MutableLiveData("")
    val admYear = MutableLiveData("")

    var isModAvailable = true

    private lateinit var myUserData: ProfileModRequestModel

    fun getUserDataFromServer() {
        viewModelScope.launch {
            profileRepository.getUserData()
                .onSuccess { profile ->
                    if (profile == null) {
                        _getUserDataResult.emit(false)
                        return@launch
                    }
                    school.value = profile.groupName
                    subGroup.value = profile.subGroupName
                    admYear.value = profile.groupAdmissionYear.toString()
                    // TODO : groupId 수정
                    myUserData = ProfileModRequestModel(
                        profile.name,
                        profile.yelloId,
                        profile.gender,
                        profile.email,
                        profile.profileImageUrl,
                        0,
                        profile.groupAdmissionYear
                    )
                    _getUserDataResult.emit(true)
                }
                .onFailure {
                    _getUserDataResult.emit(false)
                }
        }
    }

}