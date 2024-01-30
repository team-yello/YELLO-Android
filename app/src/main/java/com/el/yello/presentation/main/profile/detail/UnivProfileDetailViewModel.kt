package com.el.yello.presentation.main.profile.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.ProfileModRequestModel
import com.example.domain.repository.ProfileRepository
import com.example.ui.view.UiState
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnivProfileDetailViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _getUserDataState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val getUserDataState: StateFlow<UiState<String>> = _getUserDataState

    private val _getKakaoInfoResult = MutableSharedFlow<Boolean>()
    val getKakaoInfoResult: SharedFlow<Boolean> = _getKakaoInfoResult

    private val _postToModProfileState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val postToModProfileState: StateFlow<UiState<String>> = _postToModProfileState

    val name = MutableLiveData("")
    val id = MutableLiveData("")
    val school = MutableLiveData("")
    val subGroup = MutableLiveData("")
    val admYear = MutableLiveData("")

    private lateinit var myUserData: ProfileModRequestModel

    fun resetViewModelState() {
        _getUserDataState.value = UiState.Empty
        _postToModProfileState.value = UiState.Empty
    }


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
                    admYear.value = profile.groupAdmissionYear.toString()
                    myUserData = ProfileModRequestModel(
                        profile.name,
                        profile.yelloId,
                        profile.gender,
                        profile.email,
                        profile.profileImageUrl,
                        profile.groupId,
                        profile.groupAdmissionYear
                    )
                    _getUserDataState.value = UiState.Success(profile.profileImageUrl)
                }
                .onFailure {
                    _getUserDataState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    fun getUserInfoFromKakao() {
        UserApiClient.instance.me { user, _ ->
            try {
                myUserData.profileImageUrl = user?.kakaoAccount?.profile?.profileImageUrl.orEmpty()
                postNewProfileImageToServer()
            } catch (e: IllegalArgumentException) {
                viewModelScope.launch {
                    _getKakaoInfoResult.emit(false)
                }
            }
        }
    }

    private fun postNewProfileImageToServer() {
        if (!::myUserData.isInitialized) _postToModProfileState.value = UiState.Failure(toString())
        viewModelScope.launch {
            profileRepository.postToModUserData(myUserData)
                .onSuccess {
                    _postToModProfileState.value = UiState.Success(myUserData.profileImageUrl)
                }
                .onFailure {
                    _postToModProfileState.value = UiState.Failure(it.message.toString())
                }
        }
    }
}