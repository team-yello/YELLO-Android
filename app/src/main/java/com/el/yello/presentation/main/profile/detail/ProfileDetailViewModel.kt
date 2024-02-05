package com.el.yello.presentation.main.profile.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.main.profile.info.ProfileFragment.Companion.TYPE_UNIVERSITY
import com.example.domain.entity.ProfileModRequestModel
import com.example.domain.entity.ProfileUserModel
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
class ProfileDetailViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _getUserDataState = MutableStateFlow<UiState<ProfileUserModel>>(UiState.Empty)
    val getUserDataState: StateFlow<UiState<ProfileUserModel>> = _getUserDataState

    private val _getKakaoInfoResult = MutableSharedFlow<ImageChangeState>()
    val getKakaoInfoResult: SharedFlow<ImageChangeState> = _getKakaoInfoResult

    private val _postToModProfileState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val postToModProfileState: StateFlow<UiState<String>> = _postToModProfileState

    val name = MutableLiveData("")
    val id = MutableLiveData("")
    val school = MutableLiveData("")

    val subGroup = MutableLiveData("")
    val admYear = MutableLiveData("")

    val grade = MutableLiveData("")
    val classroom = MutableLiveData("")

    private lateinit var myUserData: ProfileModRequestModel

    fun resetViewModelState() {
        _getUserDataState.value = UiState.Empty
        _postToModProfileState.value = UiState.Empty
        _getKakaoInfoResult.resetReplayCache()
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
                    if (profile.groupType == TYPE_UNIVERSITY) {
                        subGroup.value = profile.subGroupName
                        admYear.value = profile.groupAdmissionYear.toString()
                    } else {
                        grade.value = profile.groupAdmissionYear.toString() + "학년"
                        classroom.value = profile.subGroupName + "반"
                    }
                    myUserData = ProfileModRequestModel(
                        profile.name,
                        profile.yelloId,
                        profile.gender,
                        profile.email,
                        profile.profileImageUrl,
                        profile.groupId,
                        profile.groupAdmissionYear
                    )
                    _getUserDataState.value = UiState.Success(profile)
                }
                .onFailure {
                    _getUserDataState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    fun getUserInfoFromKakao() {
        UserApiClient.instance.me { user, _ ->
            viewModelScope.launch {
                try {
                    if (myUserData.profileImageUrl != user?.kakaoAccount?.profile?.profileImageUrl) {
                        myUserData.profileImageUrl = user?.kakaoAccount?.profile?.profileImageUrl.orEmpty()
                        _getKakaoInfoResult.emit(ImageChangeState.Success)
                        postNewProfileImageToServer()
                    } else {
                        _getKakaoInfoResult.emit(ImageChangeState.NotChanged)
                    }
                } catch (e: IllegalArgumentException) {
                    _getKakaoInfoResult.emit(ImageChangeState.Error)
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