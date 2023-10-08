package com.el.yello.presentation.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.entity.PayInfoModel
import com.example.domain.entity.ProfileFriendsListModel
import com.example.domain.entity.ProfileUserModel
import com.example.domain.entity.vote.VoteCount
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.PayRepository
import com.example.domain.repository.ProfileRepository
import com.example.domain.repository.YelloRepository
import com.example.ui.view.UiState
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val yelloRepository: YelloRepository,
    private val payRepository: PayRepository
) : ViewModel() {

    private val _getUserDataState = MutableLiveData<UiState<ProfileUserModel>>(UiState.Empty)
    val getUserDataState: LiveData<UiState<ProfileUserModel>> = _getUserDataState

    private val _getFriendListState =
        MutableLiveData<UiState<ProfileFriendsListModel?>>(UiState.Empty)
    val getFriendListState: LiveData<UiState<ProfileFriendsListModel?>> = _getFriendListState

    private val _deleteUserState = MutableLiveData<UiState<Unit>>()
    val deleteUserState: LiveData<UiState<Unit>> = _deleteUserState

    private val _deleteFriendState = MutableLiveData<UiState<Unit>>()
    val deleteFriendState: LiveData<UiState<Unit>> = _deleteFriendState

    private val _kakaoLogoutState = MutableLiveData<UiState<Unit>>()
    val kakaoLogoutState: LiveData<UiState<Unit>> = _kakaoLogoutState

    private val _kakaoQuitState = MutableLiveData<UiState<Unit>>()
    val kakaoQuitState: LiveData<UiState<Unit>> = _kakaoQuitState

    private val _getPurchaseInfoState = MutableStateFlow<UiState<PayInfoModel?>>(UiState.Empty)
    val getPurchaseInfoState: StateFlow<UiState<PayInfoModel?>> =
        _getPurchaseInfoState.asStateFlow()

    var isSubscribed: Boolean = false

    private val _voteCount = MutableStateFlow<UiState<VoteCount>>(UiState.Loading)
    val voteCount: StateFlow<UiState<VoteCount>> = _voteCount.asStateFlow()

    var isItemBottomSheetRunning: Boolean = false

    var isFirstScroll: Boolean = true

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    var myUserData = ProfileUserModel(0, "", "", "", "", 0, 0, 0)
    var myFriendCount = 0

    var clickedUserData = ProfileUserModel(0, "", "", "", "", 0, 0, 0)
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

    fun setIsFirstLoginData() {
        authRepository.setIsFirstLoginData()
    }

    // 서버 통신 - 유저 정보 받아오기
    fun getUserDataFromServer() {
        viewModelScope.launch {
            _getUserDataState.value = UiState.Loading
            profileRepository.getUserData().onSuccess { profile ->
                    if (profile == null) {
                        _getUserDataState.value = UiState.Empty
                        return@launch
                    }
                    _getUserDataState.value = UiState.Success(profile)
                }.onFailure { t ->
                    if (t is HttpException) {
                        _getUserDataState.value = UiState.Failure(t.message.toString())
                    }
                }
        }
    }

    // 서버 통신 - 친구 목록 정보 받아오기
    fun getFriendsListFromServer() {
        if (isPagingFinish) return
        if (isFirstScroll) {
            _getFriendListState.value = UiState.Loading
            isFirstScroll = false
        }
        viewModelScope.launch {
            profileRepository.getFriendsData(
                ++currentPage,
            ).onSuccess {
                    it ?: return@launch
                    totalPage = ceil((it.totalCount * 0.1)).toInt() - 1
                    if (totalPage == currentPage) isPagingFinish = true
                    _getFriendListState.value = UiState.Success(it)
                    AmplitudeUtils.updateUserIntProperties("user_friends", it.totalCount)
                }.onFailure {
                    _getFriendListState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    // 서버 통신 - 회원 탈퇴
    fun deleteUserDataToServer() {
        viewModelScope.launch {
            _deleteUserState.value = UiState.Loading
            profileRepository.deleteUserData().onSuccess {
                    clearLocalInfo()
                    delay(300)
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
            profileRepository.deleteFriendData(friendId).onSuccess {
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
                clearLocalInfo()
            } else {
                _kakaoLogoutState.value = UiState.Failure(error.message.toString())
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
                _kakaoQuitState.value = UiState.Failure(error.message.toString())
            }
        }
    }

    // 서버 통신 - 구독 여부 & 열람권 개수 받아오기
    fun getPurchaseInfoFromServer() {
        viewModelScope.launch {
            payRepository.getPurchaseInfo().onSuccess {
                    it ?: return@launch
                    val subStatus: String = if (it.isSubscribe) "yes" else "no"
                    AmplitudeUtils.updateUserProperties("user_subscription", subStatus)
                    AmplitudeUtils.updateUserIntProperties("user_ticket", it.ticketCount)
                    _getPurchaseInfoState.value = UiState.Success(it)
                }.onFailure {
                    _getPurchaseInfoState.value = UiState.Failure(it.message.toString())
                }
        }

    }

    fun getVoteCount() {
        viewModelScope.launch {
            yelloRepository.voteCount().onSuccess {
                    if (it != null) {
                        _voteCount.value = UiState.Success(it)
                    }
                }.onFailure {
                    _voteCount.value = UiState.Failure(it.message.toString())
                }
        }
    }

    private fun clearLocalInfo() {
        authRepository.clearLocalPref()
    }

    fun putDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { addTask ->
            runCatching {
                addTask.result
            }.onSuccess { token ->
                if (authRepository.getDeviceToken() != token) resetDeviceToken(token)
            }
        }
    }

    private fun resetDeviceToken(token: String) {
        authRepository.setDeviceToken(token)
        viewModelScope.launch {
            runCatching {
                authRepository.putDeviceToken(token)
            }.onFailure(Timber::e)
        }
    }

    companion object {
        const val BASIC_THUMBNAIL =
            "https://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg"
    }
}
