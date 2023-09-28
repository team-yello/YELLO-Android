package com.el.yello.presentation.pay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RequestPayModel
import com.example.domain.entity.ResponsePayInAppModel
import com.example.domain.entity.ResponsePaySubsModel
import com.example.domain.entity.ResponsePurchaseInfoModel
import com.example.domain.entity.ResponseSubsNeededModel
import com.example.domain.repository.PayRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val payRepository: PayRepository
) : ViewModel() {

    var currentInAppItem: String = ""

    private val _postSubsCheckState = MutableLiveData<UiState<ResponsePaySubsModel?>>()
    val postSubsCheckState: LiveData<UiState<ResponsePaySubsModel?>> = _postSubsCheckState

    private val _postInAppCheckState = MutableLiveData<UiState<ResponsePayInAppModel?>>()
    val postInAppCheckState: LiveData<UiState<ResponsePayInAppModel?>> = _postInAppCheckState

    private val _getSubsNeededState = MutableLiveData<UiState<ResponseSubsNeededModel?>>()
    val getSubsNeededState: LiveData<UiState<ResponseSubsNeededModel?>> = _getSubsNeededState

    private val _getPurchaseInfoState = MutableLiveData<UiState<ResponsePurchaseInfoModel?>>()
    val getPurchaseInfoState: LiveData<UiState<ResponsePurchaseInfoModel?>> = _getPurchaseInfoState

   var ticketCount = 0

    fun setTicketCount(count: Int) {
        ticketCount = count
    }

    fun addTicketCount(count: Int) {
        ticketCount += count
    }

    // 서버 통신 - 구독 상품 검증
    fun checkSubsToServer(request: RequestPayModel) {
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
    fun checkInAppToServer(request: RequestPayModel) {
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

    // 서버 통신 - (아직 사용 X) 구독 재촉 알림 필요 여부 확인
    fun getSubsNeededFromServer() {
        viewModelScope.launch {
            payRepository.getSubsNeeded()
                .onSuccess {
                    _getSubsNeededState.value = UiState.Success(it)
                }
                .onFailure {
                    _getSubsNeededState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    // 서버 통신 - 구독 여부 & 열람권 개수 받아오기
    fun getPurchaseInfoFromServer() {
        viewModelScope.launch {
            payRepository.getPurchaseInfo()
                .onSuccess {
                    _getPurchaseInfoState.value = UiState.Success(it)
                }
                .onFailure {
                    _getPurchaseInfoState.value = UiState.Failure(it.message.toString())
                }
        }
    }
}
