package com.yello.presentation.main.profile

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
import kotlin.math.ceil

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _getState = MutableLiveData<UiState<ProfileUserModel?>>()
    val getState: LiveData<UiState<ProfileUserModel?>> = _getState

    private val _getListState = MutableLiveData<UiState<ProfileFriendsModel?>>()
    val getListState: LiveData<UiState<ProfileFriendsModel?>> = _getListState

    private val _deleteUserState = MutableLiveData<UiState<Unit>>()
    val deleteUserState: LiveData<UiState<Unit>> = _deleteUserState

    // TODO: 세터 설정 필요
    val _deleteFriendState = MutableLiveData<UiState<Unit>>()
    val deleteFriendState: LiveData<UiState<Unit>> = _deleteFriendState

    var currentPage = -1
    var isPagingFinish = false
    var totalPage = Int.MAX_VALUE

    val myName: MutableLiveData<String> = MutableLiveData("")
    val myId: MutableLiveData<String> = MutableLiveData("")
    val mySchool: MutableLiveData<String> = MutableLiveData("")
    val myThumbnail: MutableLiveData<String> = MutableLiveData("")
    val myTotalMsg: MutableLiveData<String> = MutableLiveData("")
    val myTotalFriends: MutableLiveData<String> = MutableLiveData("")
    val myTotalPoints: MutableLiveData<String> = MutableLiveData("")

    val clickedItemId: MutableLiveData<Int> = MutableLiveData()
    val clickedItemName: MutableLiveData<String> = MutableLiveData("")
    val clickedItemYelloId: MutableLiveData<String> = MutableLiveData("")
    val clickedItemSchool: MutableLiveData<String> = MutableLiveData("")
    val clickedItemThumbnail: MutableLiveData<String> = MutableLiveData("")
    val clickedItemTotalMsg: MutableLiveData<String> = MutableLiveData("")
    val clickedItemTotalFriends: MutableLiveData<String> = MutableLiveData("")

    var clickedItemPosition: Int? = null

    fun setItemPosition(position: Int) {
        clickedItemPosition = position
    }

    fun getUserDataFromServer(userId: Int) {
        viewModelScope.launch {
            _getState.value = UiState.Loading
            runCatching {
                profileRepository.getUserData(userId)
            }.onSuccess {
                _getState.value = UiState.Success(it)
            }.onFailure {
                _getState.value = UiState.Failure(it.message.toString())
            }
        }
    }

    fun getFriendsListFromServer() {
        viewModelScope.launch {
            if (isPagingFinish) return@launch
            runCatching {
                profileRepository.getFriendsData(
                    ++currentPage
                )
            }.onSuccess {
                it ?: return@launch
                totalPage = ceil((it.totalCount * 0.1)).toInt() - 1
                if (totalPage == currentPage) isPagingFinish = true
                _getListState.value = UiState.Success(it)
            }.onFailure {
                _getListState.value = UiState.Failure(it.message.toString())
            }
        }
    }

    fun deleteUserDataToServer() {
        viewModelScope.launch {
            _deleteUserState.value = UiState.Loading
            runCatching {
                profileRepository.deleteUserData()
            }.onSuccess {
                _deleteUserState.value = UiState.Success(it)
            }.onFailure {
                _deleteUserState.value = UiState.Failure(it.message.toString())
            }
        }
    }

    fun deleteFriendDataToServer(friendId: Int) {
        viewModelScope.launch {
            _deleteFriendState.value = UiState.Loading
            runCatching {
                profileRepository.deleteFriendData(
                    friendId
                )
            }.onSuccess {
                _deleteFriendState.value = UiState.Success(it)
            }.onFailure {
                _deleteFriendState.value = UiState.Failure(it.message.toString())
            }
        }
    }
}