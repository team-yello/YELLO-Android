package com.el.yello.presentation.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.PayInAppModel
import com.example.domain.entity.PayInfoModel
import com.example.domain.entity.PayRequestModel
import com.example.domain.entity.PaySubsModel
import com.example.domain.repository.PayRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val payRepository: PayRepository,
) : ViewModel() {

    var currentInAppItem = String()

    private val _postSubsCheckState = MutableStateFlow<UiState<PaySubsModel?>>(UiState.Empty)
    val postSubsCheckState: StateFlow<UiState<PaySubsModel?>> = _postSubsCheckState

    private val _postInAppCheckState = MutableStateFlow<UiState<PayInAppModel?>>(UiState.Empty)
    val postInAppCheckState: StateFlow<UiState<PayInAppModel?>> = _postInAppCheckState

    private val _getPurchaseInfoState = MutableStateFlow<UiState<PayInfoModel?>>(UiState.Empty)
    val getPurchaseInfoState: StateFlow<UiState<PayInfoModel?>> = _getPurchaseInfoState

    var ticketCount = 0
        private set

    fun setTicketCount(count: Int) {
        ticketCount = count
    }

    fun addTicketCount(count: Int) {
        ticketCount += count
    }

    // 서버 통신 - 구독 상품 검증
    fun checkSubsToServer(request: PayRequestModel) {
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

    // 서버 통신 - 열람권 상품 검증
    fun checkInAppToServer(request: PayRequestModel) {
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

    // 서버 통신 - 구독 여부 & 열람권 개수 받아오기
    fun getPurchaseInfoFromServer() {
        viewModelScope.launch {
            payRepository.getPurchaseInfo()
                .onSuccess {
                    it ?: return@launch
                    _getPurchaseInfoState.value = UiState.Success(it)
                }
                .onFailure {
                    _getPurchaseInfoState.value = UiState.Failure(it.message.toString())
                }
        }
    }
}
