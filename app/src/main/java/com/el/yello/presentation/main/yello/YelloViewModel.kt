package com.el.yello.presentation.main.yello

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.main.yello.YelloState.Lock
import com.el.yello.presentation.main.yello.YelloState.Valid
import com.el.yello.presentation.main.yello.YelloState.Wait
import com.example.domain.entity.PayInfoModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.PayRepository
import com.example.domain.repository.VoteRepository
import com.example.ui.state.UiState
import com.example.ui.state.UiState.Empty
import com.example.ui.state.UiState.Failure
import com.example.ui.state.UiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class YelloViewModel @Inject constructor(
    private val voteRepository: VoteRepository,
    private val authRepository: AuthRepository,
    private val payRepository: PayRepository,
) : ViewModel() {
    private val _yelloState = MutableStateFlow<UiState<YelloState>>(UiState.Loading)
    val yelloState: StateFlow<UiState<YelloState>>
        get() = _yelloState.asStateFlow()

    private val _leftTime = MutableStateFlow<Long>(0)
    val leftTime: StateFlow<Long>
        get() = _leftTime.asStateFlow()

    private val _point = MutableStateFlow(0)
    val point: StateFlow<Int>
        get() = _point.asStateFlow()

    private val _isDecreasing = MutableStateFlow(false)
    private val isDecreasing: Boolean
        get() = _isDecreasing.value

    private val _getPurchaseInfoState =
        MutableStateFlow<UiState<PayInfoModel>>(UiState.Loading)
    val getPurchaseInfoState: StateFlow<UiState<PayInfoModel>> =
        _getPurchaseInfoState.asStateFlow()

    private fun decreaseTime() {
        if (isDecreasing) return
        viewModelScope.launch {
            _isDecreasing.value = true
            while (requireNotNull(leftTime.value) > 0) {
                delay(1000L)
                if (requireNotNull(leftTime.value) <= 0) return@launch
                _leftTime.value = leftTime.value - 1
            }

            getVoteState()
            _isDecreasing.value = false
        }
    }

    fun getVoteState() {
        viewModelScope.launch {
            voteRepository.getVoteAvailable()
                .onSuccess { voteState ->
                    if (voteState == null) {
                        _yelloState.value = Empty
                        return@launch
                    }

                    Timber.tag("GET_VOTE_STATE_SUCCESS").d(voteState.toString())
                    _point.value = voteState.point
                    if (voteState.isStart || voteState.leftTime !in 1..SEC_MAX_LOCK_TIME) {
                        val currentState = yelloState.value
                        if (currentState is Success) {
                            if (currentState.data is Valid) return@onSuccess
                        }
                        _yelloState.value =
                            Success(Valid(voteState.point, voteState.hasFourFriends))
                        return@launch
                    }

                    _yelloState.value = Success(Wait(voteState.leftTime))
                    _leftTime.value = voteState.leftTime
                    decreaseTime()
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("GET VOTE STATE FAILURE : $t")
                        when (t.code()) {
                            CODE_NO_FRIEND -> _yelloState.value = Success(Lock)
                            else -> {
                                authRepository.clearLocalPref()
                                delay(500)
                                _yelloState.value = Failure(t.code().toString())
                            }
                        }
                    }
                    Timber.e("GET VOTE STATE ERROR : $t")
                }
        }
    }

    fun getPurchaseInfoFromServer() {
        viewModelScope.launch {
            payRepository.getPurchaseInfo()
                .onSuccess { purchaseInfo ->
                    if (purchaseInfo == null) {
                        _getPurchaseInfoState.value = Empty
                        return@onSuccess
                    }
                    _getPurchaseInfoState.value = Success(purchaseInfo)
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        _getPurchaseInfoState.value = Failure(t.code().toString())
                    }
                }
        }
    }

    fun getYelloId() = authRepository.getYelloId()

    fun getStoredVote() = voteRepository.getStoredVote()

    fun navigateToLockScreen() {
        _yelloState.value = Success(Wait(SEC_MAX_LOCK_TIME))
    }

    companion object {
        const val SEC_MAX_LOCK_TIME = 2400L

        private const val CODE_NO_FRIEND = 400
    }
}
