package com.el.yello.presentation.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.PayInAppModel
import com.example.domain.entity.PayRequestModel
import com.example.domain.entity.PaySubsModel
import com.example.domain.entity.ProfileUserModel
import com.example.domain.entity.event.RewardAdModel
import com.example.domain.entity.event.RewardAdRequestModel
import com.example.domain.repository.EventRepository
import com.example.domain.repository.PayRepository
import com.example.domain.repository.ProfileRepository
import com.example.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val payRepository: PayRepository,
    private val profileRepository: ProfileRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    var currentInAppItem = String()

    private val _postSubsCheckState = MutableStateFlow<UiState<PaySubsModel?>>(UiState.Empty)
    val postSubsCheckState: StateFlow<UiState<PaySubsModel?>> = _postSubsCheckState

    private val _postInAppCheckState = MutableStateFlow<UiState<PayInAppModel?>>(UiState.Empty)
    val postInAppCheckState: StateFlow<UiState<PayInAppModel?>> = _postInAppCheckState

    private val _getUserInfoState = MutableStateFlow<UiState<ProfileUserModel?>>(UiState.Empty)
    val getUserInfoState: StateFlow<UiState<ProfileUserModel?>> = _getUserInfoState

    private val _postRewardAdState = MutableStateFlow<UiState<RewardAdModel?>>(UiState.Empty)
    val postRewardAdState: StateFlow<UiState<RewardAdModel?>> = _postRewardAdState

    private val _getRewardAdPossibleState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val getRewardAdPossibleState: StateFlow<UiState<Boolean>> = _getRewardAdPossibleState

    var isAdAvailable: Boolean = true

    private val _leftTime = MutableStateFlow<Long>(0)
    val leftTime: StateFlow<Long>
        get() = _leftTime.asStateFlow()

    private val _isDecreasing = MutableStateFlow(false)
    private val isDecreasing: Boolean
        get() = _isDecreasing.value

    private var _idempotencyKey: UUID? = null
    val idempotencyKey: UUID get() = requireNotNull(_idempotencyKey)

    var ticketCount = 0
        private set

    var pointCount = 0
        private set

    fun setTicketCount(count: Int) {
        ticketCount = count
    }

    fun addTicketCount(count: Int) {
        ticketCount += count
    }

    fun setPointCount(count: Int) {
        pointCount = count
    }

    fun addPointCount(count: Int) {
        pointCount += count
    }

    fun checkSubsValidToServer(request: PayRequestModel) {
        viewModelScope.launch {
            _postSubsCheckState.value = UiState.Loading
            payRepository.postToCheckSubs(request)
                .onSuccess {
                    _postSubsCheckState.value = UiState.Success(it)
                }
                .onFailure {
                    _postSubsCheckState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    fun checkInAppValidToServer(request: PayRequestModel) {
        viewModelScope.launch {
            _postInAppCheckState.value = UiState.Loading
            payRepository.postToCheckInApp(request)
                .onSuccess {
                    _postInAppCheckState.value = UiState.Success(it)
                }
                .onFailure {
                    _postInAppCheckState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    fun getUserDataFromServer() {
        viewModelScope.launch {
            _getUserInfoState.value = UiState.Loading
            profileRepository.getUserData()
                .onSuccess { info ->
                    if (info == null) {
                        _getUserInfoState.value = UiState.Empty
                    } else {
                        _getUserInfoState.value = UiState.Success(info)
                    }
                }
                .onFailure { t ->
                    _getUserInfoState.value = UiState.Failure(t.message.orEmpty())
                }
        }
    }

    fun setUuid() {
        _idempotencyKey = UUID.randomUUID()
    }

    fun postRewardAdToCheck() {
        viewModelScope.launch {
            _postRewardAdState.value = UiState.Loading
            eventRepository.postRewardAd(
                idempotencyKey.toString(),
                RewardAdRequestModel(
                    REWARD_TYPE_POINT,
                    RANDOM_TYPE_FIXED,
                    idempotencyKey.toString(),
                    REWARD_NUMBER
                )
            )
                .onSuccess { reward ->
                    if (reward == null) {
                        _postRewardAdState.value = UiState.Failure(toString())
                    } else {
                        _postRewardAdState.value = UiState.Success(reward)
                    }
                }
                .onFailure {
                    _postRewardAdState.value = UiState.Failure(it.message.orEmpty())
                }
        }
    }

    fun getRewardAdPossible() {
        viewModelScope.launch {
            _getRewardAdPossibleState.value = UiState.Loading
            eventRepository.getRewardAdPossible()
                .onSuccess { result ->
                    if (result == null) {
                        _getRewardAdPossibleState.value = UiState.Empty
                        return@launch
                    }
                    if (!result.isPossible) {
                        _leftTime.value = result.leftTime
                        decreaseTime()
                    }
                    isAdAvailable = result.isPossible
                    _getRewardAdPossibleState.value = UiState.Success(result.isPossible)
                }
                .onFailure {
                    _getRewardAdPossibleState.value = UiState.Failure(it.message.orEmpty())
                }
        }
    }

    private fun decreaseTime() {
        if (isDecreasing) return
        viewModelScope.launch {
            _isDecreasing.value = true
            while (requireNotNull(leftTime.value) > 0) {
                delay(1000L)
                if (requireNotNull(leftTime.value) <= 0) return@launch
                _leftTime.value = leftTime.value - 1
            }
            getRewardAdPossible()
            _isDecreasing.value = false
        }
    }

    companion object {
        private const val REWARD_TYPE_POINT = "ADMOB_POINT"
        const val RANDOM_TYPE_FIXED = "FIXED"
        private const val REWARD_NUMBER = 10
    }

}
