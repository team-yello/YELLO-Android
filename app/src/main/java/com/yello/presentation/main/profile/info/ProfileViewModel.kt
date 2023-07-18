package com.yello.presentation.main.profile.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.ProfileFriendsModel
import com.example.domain.entity.ProfileUserModel
import com.example.domain.repository.ProfileRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _getState = MutableLiveData<UiState<ProfileUserModel?>>()
    val getState: LiveData<UiState<ProfileUserModel?>> = _getState

    private val _getListState = MutableLiveData<UiState<ProfileFriendsModel?>>()
    val getListState: LiveData<UiState<ProfileFriendsModel?>> = _getListState

    val myName: MutableLiveData<String> = MutableLiveData("")
    val myId: MutableLiveData<String> = MutableLiveData("")
    val mySchool: MutableLiveData<String> = MutableLiveData("")
    val myThumbnail: MutableLiveData<String> = MutableLiveData("")
    val myTotalMsg: MutableLiveData<String> = MutableLiveData("")
    val myTotalFriends: MutableLiveData<String> = MutableLiveData("")
    val myTotalPoints: MutableLiveData<String> = MutableLiveData("")

    val clickedItemName: MutableLiveData<String> = MutableLiveData("")
    val clickedItemId: MutableLiveData<String> = MutableLiveData("")
    val clickedItemSchool: MutableLiveData<String> = MutableLiveData("")
    val clickedItemThumbnail: MutableLiveData<String> = MutableLiveData("")
    val clickedItemTotalMsg: MutableLiveData<String> = MutableLiveData("")
    val clickedItemTotalFriends: MutableLiveData<String> = MutableLiveData("")

    fun getUserDataFromServer(userId: Int) {
        viewModelScope.launch {
            _getState.value = UiState.Loading
            runCatching {
                profileRepository.getUserData(userId)
            }.onSuccess {
                _getState.value = UiState.Success(it)
            }.onFailure {
                _getState.value = UiState.Failure("유저 정보 서버 통신 실패")
            }
        }
    }

    fun getFriendsDataFromServer(page: Int) {
        viewModelScope.launch {
            _getListState.value = UiState.Loading
            runCatching {
                profileRepository.getFriendsData(page)
            }.onSuccess {
                _getListState.value = UiState.Success(it)
            }.onFailure {
                _getListState.value = UiState.Failure("유저 정보 서버 통신 실패")
            }
        }
    }
}