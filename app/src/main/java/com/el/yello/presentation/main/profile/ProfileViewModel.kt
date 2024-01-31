package com.el.yello.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.entity.PayInfoModel
import com.example.domain.entity.PayUserSubsInfoModel
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val yelloRepository: YelloRepository,
    private val payRepository: PayRepository,
) : ViewModel() {

    init {
        resetPageVariable()
        // resetStateVariable()
    }

    private val _getUserDataResult = MutableSharedFlow<Boolean>()
    val getUserDataResult: SharedFlow<Boolean> = _getUserDataResult

    private val _getFriendListState =
        MutableStateFlow<UiState<ProfileFriendsListModel>>(UiState.Empty)
    val getFriendListState: StateFlow<UiState<ProfileFriendsListModel>> = _getFriendListState

    private val _deleteUserState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val deleteUserState: StateFlow<UiState<Unit>> = _deleteUserState

    private val _deleteFriendState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val deleteFriendState: StateFlow<UiState<Unit>> = _deleteFriendState

    private val _kakaoLogoutState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val kakaoLogoutState: StateFlow<UiState<Unit>> = _kakaoLogoutState

    private val _kakaoQuitState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val kakaoQuitState: StateFlow<UiState<Unit>> = _kakaoQuitState

    private val _getPurchaseInfoState = MutableStateFlow<UiState<PayInfoModel>>(UiState.Empty)
    val getPurchaseInfoState: StateFlow<UiState<PayInfoModel>> = _getPurchaseInfoState

    var isSubscribed: Boolean = false

    private val _voteCount = MutableStateFlow<UiState<VoteCount>>(UiState.Loading)
    val voteCount: StateFlow<UiState<VoteCount>> = _voteCount

    private val _getUserSubsInfoState =
        MutableStateFlow<UiState<PayUserSubsInfoModel?>>(UiState.Empty)
    val getUserSubsInfoState: StateFlow<UiState<PayUserSubsInfoModel?>> = _getUserSubsInfoState

    var isItemBottomSheetRunning: Boolean = false

    private var isFirstScroll: Boolean = true

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    var myUserData = ProfileUserModel()
    var myFriendCount = 0

    var clickedUserData = ProfileUserModel()
    var clickedItemPosition: Int? = null

    fun setItemPosition(position: Int) {
        clickedItemPosition = position
    }

    fun setDeleteFriendStateEmpty() {
        _deleteFriendState.value = UiState.Empty
    }

    fun resetPageVariable() {
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
    }

    fun resetStateVariable() {
        _deleteFriendState.value = UiState.Empty
        _deleteUserState.value = UiState.Empty
        _kakaoLogoutState.value = UiState.Empty
        _kakaoQuitState.value = UiState.Empty
        _getFriendListState.value = UiState.Empty
        _getPurchaseInfoState.value = UiState.Empty
    }

    fun setIsFirstLoginData() {
        authRepository.setIsFirstLoginData()
    }

    fun getUserDataFromServer() {
        viewModelScope.launch {
            profileRepository.getUserData()
                .onSuccess { profile ->
                    if (profile == null) return@launch
                    myUserData = profile.apply {
                        if (!this.yelloId.startsWith("@")) this.yelloId = "@" + this.yelloId
                    }
                    myFriendCount = profile.friendCount
                }
                .onFailure { t ->
                    _getUserDataResult.emit(false)
                }
        }
    }

    fun getFriendsListFromServer() {
        if (isPagingFinish) return
        if (isFirstScroll) {
            _getFriendListState.value = UiState.Loading
            isFirstScroll = false
        }
        viewModelScope.launch {
            profileRepository.getFriendsData(
                ++currentPage,
            )
                .onSuccess {
                    it ?: return@launch
                    totalPage = ceil((it.totalCount * 0.1)).toInt() - 1
                    if (totalPage == currentPage) isPagingFinish = true
                    _getFriendListState.value = UiState.Success(it)
                    AmplitudeUtils.updateUserIntProperties("user_friends", it.totalCount)
                }
                .onFailure {
                    _getFriendListState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    fun deleteUserDataToServer() {
        viewModelScope.launch {
            _deleteUserState.value = UiState.Loading
            profileRepository.deleteUserData()
                .onSuccess {
                    clearLocalInfo()
                    delay(300)
                    _deleteUserState.value = UiState.Success(it)
                }
                .onFailure {
                    _deleteUserState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    fun deleteFriendDataToServer(friendId: Long) {
        viewModelScope.launch {
            _deleteFriendState.value = UiState.Loading
            profileRepository.deleteFriendData(friendId)
                .onSuccess {
                    _deleteFriendState.value = UiState.Success(it)
                }
                .onFailure {
                    _deleteFriendState.value = UiState.Failure(it.message.toString())
                }
        }
    }

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

    fun getPurchaseInfoFromServer() {
        viewModelScope.launch {
            payRepository.getPurchaseInfo()
                .onSuccess {
                    it ?: return@launch
                    val subStatus: String = if (it.isSubscribe) "yes" else "no"
                    AmplitudeUtils.updateUserProperties("user_subscription", subStatus)
                    AmplitudeUtils.updateUserIntProperties("user_ticket", it.ticketCount)
                    _getPurchaseInfoState.value = UiState.Success(it)
                }
                .onFailure {
                    _getPurchaseInfoState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    fun getVoteCount() {
        viewModelScope.launch {
            yelloRepository.voteCount()
                .onSuccess {
                    if (it != null) {
                        _voteCount.value = UiState.Success(it)
                    }
                }
                .onFailure {
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

    fun getUserSubsInfoStateFromServer() {
        viewModelScope.launch {
            payRepository.getUserSubsInfo()
                .onSuccess { userInfo ->
                    if (userInfo == null) {
                        _getUserSubsInfoState.value = UiState.Empty
                    } else {
                        _getUserSubsInfoState.value = UiState.Success(userInfo)
                    }
                }
                .onFailure {
                    _getUserSubsInfoState.value = UiState.Failure(it.message.toString())
                }
        }
    }
}
