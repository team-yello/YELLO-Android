package com.el.yello.presentation.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.ProfileFriendsListModel
import com.example.domain.entity.ProfileUserModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.ProfileRepository
import com.example.ui.view.UiState
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _getState = MutableLiveData<UiState<ProfileUserModel>>()
    val getState: LiveData<UiState<ProfileUserModel>> = _getState

    private val _getListState = MutableLiveData<UiState<ProfileFriendsListModel?>>(UiState.Loading)
    val getListState: LiveData<UiState<ProfileFriendsListModel?>> = _getListState

    private val _deleteUserState = MutableLiveData<UiState<Unit>>()
    val deleteUserState: LiveData<UiState<Unit>> = _deleteUserState

    private val _deleteFriendState = MutableLiveData<UiState<Unit>>()
    val deleteFriendState: LiveData<UiState<Unit>> = _deleteFriendState

    private val _kakaoLogoutState = MutableLiveData<UiState<Unit>>()
    val kakaoLogoutState: LiveData<UiState<Unit>> = _kakaoLogoutState

    private val _kakaoQuitState = MutableLiveData<UiState<Unit>>()
    val kakaoQuitState: LiveData<UiState<Unit>> = _kakaoQuitState

    var isBottomSheetRunning: Boolean = false

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

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

    fun setDeleteFriendStateEmpty() {
        _deleteFriendState.value = UiState.Empty
    }

    fun initPagingVariable() {
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
    }

    // 서버 통신 - 유저 정보 받아오기
    fun getUserDataFromServer() {
        viewModelScope.launch {
            _getState.value = UiState.Loading
            runCatching {
                profileRepository.getUserData()
            }.onSuccess { profile ->
                if (profile == null) {
                    _getState.value = UiState.Empty
                    return@launch
                }
                _getState.value = UiState.Success(profile)
            }.onFailure { t ->
                if (t is HttpException) {
                    _getState.value = UiState.Failure(t.message.toString())
                }
            }
        }
    }

    // 서버 통신 - 친구 목록 정보 받아오기 & 페이징 적용
    fun getFriendsListFromServer() {
        if (isPagingFinish) return
        _getListState.value = UiState.Loading
        viewModelScope.launch {
            runCatching {
                profileRepository.getFriendsData(
                    ++currentPage,
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

    // 서버 통신 - 회원 탈퇴
    fun deleteUserDataToServer() {
        viewModelScope.launch {
            _deleteUserState.value = UiState.Loading
            runCatching {
                profileRepository.deleteUserData()
                clearLocalInfo()
                delay(500)
            }.onSuccess {
                _deleteUserState.value = UiState.Success(it)
            }.onFailure {
                _deleteUserState.value = UiState.Failure(it.message.toString())
            }
        }
    }

    // 서버 통신 - 친구 식제
    fun deleteFriendDataToServer(friendId: Int) {
        viewModelScope.launch {
            _deleteFriendState.value = UiState.Loading
            runCatching {
                profileRepository.deleteFriendData(
                    friendId,
                )
            }.onSuccess {
                _deleteFriendState.value = UiState.Success(it)
            }.onFailure {
                _deleteFriendState.value = UiState.Failure(it.message.toString())
            }
        }
    }

    // 카카오 SDK 통신 - 로그아웃
    fun logoutKakaoAccount() {
        UserApiClient.instance.logout { error ->
            _kakaoLogoutState.value = UiState.Loading
            if (error == null) {
                _kakaoLogoutState.value = UiState.Success(Unit)
            } else {
                _kakaoLogoutState.value = UiState.Failure(error.message ?: "")
            }
        }
    }

    // 카카오 SDK 통신 - 회원 탈퇴
    fun quitKakaoAccount() {
        UserApiClient.instance.unlink { error ->
            _kakaoQuitState.value = UiState.Loading
            if (error == null) {
                _kakaoQuitState.value = UiState.Success(Unit)
            } else {
                _kakaoQuitState.value = UiState.Failure(error.message ?: "")
            }
        }
    }

    fun clearLocalInfo() {
        authRepository.clearLocalPref()
    }
}
