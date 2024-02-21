package com.el.yello.presentation.main.profile.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.entity.PayInfoModel
import com.example.domain.entity.ProfileFriendsListModel
import com.example.domain.entity.ProfileUserModel
import com.example.domain.entity.notice.ProfileBanner
import com.example.domain.repository.NoticeRepository
import com.example.domain.repository.PayRepository
import com.example.domain.repository.ProfileRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
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

    private val _deleteFriendState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val deleteFriendState: StateFlow<UiState<Unit>> = _deleteFriendState

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

    private val _getBannerResult = MutableSharedFlow<Boolean>()
    val getBannerResult: SharedFlow<Boolean> = _getBannerResult

    var profileBanner = ProfileBanner()

    fun setItemPosition(position: Int) {
        clickedItemPosition = position
    }

    fun setDeleteFriendStateEmpty() {
        _deleteFriendState.value = UiState.Empty
    }

    fun resetProfileData() {
        resetPageVariable()
        resetStateVariable()
        getPurchaseInfoFromServer()
        getUserDataFromServer()
        getProfileBannerFromServer()
    }

    private fun resetPageVariable() {
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
    }

    fun resetStateVariable() {
        _deleteFriendState.value = UiState.Empty
        _getFriendListState.value = UiState.Empty
        _getPurchaseInfoState.value = UiState.Empty
        _getUserDataResult.resetReplayCache()
        _getBannerResult.resetReplayCache()
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

    private fun getPurchaseInfoFromServer() {
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

    private fun getProfileBannerFromServer() {
        viewModelScope.launch {
            noticeRepository.getProfileBanner()
                .onSuccess { banner ->
                    profileBanner = banner ?: return@launch
                    _getBannerResult.emit(true)
                }
                .onFailure {
                    _getBannerResult.emit(false)
                }
        }
    }
}
