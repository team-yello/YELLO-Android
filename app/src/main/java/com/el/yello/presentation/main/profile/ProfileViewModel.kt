package com.el.yello.presentation.main.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.R
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.entity.PayInfoModel
import com.example.domain.entity.ProfileFriendsListModel
import com.example.domain.entity.ProfileQuitReasonModel
import com.example.domain.entity.ProfileUserModel
import com.example.domain.entity.notice.Banner
import com.example.domain.entity.notice.ProfileBanner
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.NoticeRepository
import com.example.domain.repository.PayRepository
import com.example.domain.repository.ProfileRepository
import com.example.ui.view.UiState
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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
    private val payRepository: PayRepository,
    private val noticeRepository: NoticeRepository
) : ViewModel() {

    init {
        resetPageVariable()
    }

    private val _getUserDataResult = MutableSharedFlow<Boolean>()
    val getUserDataResult: SharedFlow<Boolean> = _getUserDataResult

    private val _getFriendListState = MutableStateFlow<UiState<ProfileFriendsListModel>>(UiState.Empty)
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

    var isItemBottomSheetRunning: Boolean = false

    private var isFirstScroll: Boolean = true

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    var myUserData = ProfileUserModel()
    var myFriendCount = 0

    var clickedUserData = ProfileUserModel()
    var clickedItemPosition: Int? = null

    private val quitReasonText = MutableLiveData<String>()
    val etcText = MutableLiveData("")
    private val _quitReasonData: MutableLiveData<List<String>> = MutableLiveData()
    val quitReasonData: LiveData<List<String>> = _quitReasonData

    private val _getBannerState = MutableSharedFlow<Boolean>()
    val getBannerState: SharedFlow<Boolean> = _getBannerState

    var profileBanner = ProfileBanner()

    fun setEtcText(etc: String) {
        etcText.value = etc
    }

    fun setQuitReason(reason: String) {
        quitReasonText.value = reason
    }

    fun addQuitReasonList(context: Context) {
        val quitReasonArray = context.resources.getStringArray(R.array.quit_reasons)
        _quitReasonData.value = quitReasonArray.toList()
    }

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
        _getUserDataResult.resetReplayCache()
        _getBannerState.resetReplayCache()
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
            val quitReason = if (quitReasonText.value.toString().equals("기타")) {
                etcText.value.toString()
            } else {
                quitReasonText.value.toString()
            }
            profileRepository.deleteUserData(
                ProfileQuitReasonModel(
                    quitReason,
                ),
            )
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

   fun getProfileBannerFromServer() {
        viewModelScope.launch {
            noticeRepository.getProfileBanner()
                .onSuccess { banner ->
                    Timber.tag("sangho").d("$banner")
                    _getBannerState.emit(true)
                    profileBanner = banner ?: return@launch
                    Timber.tag("sangho").d("$profileBanner")
                }
                .onFailure {
                    _getBannerState.emit(false)
                }
        }
    }

    private fun clearLocalInfo() {
        authRepository.clearLocalPref()
    }
}
