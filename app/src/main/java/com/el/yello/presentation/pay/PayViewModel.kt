package com.el.yello.presentation.pay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RequestPayModel
import com.example.domain.entity.Response
import com.example.domain.entity.ResponsePayInAppModel
import com.example.domain.entity.ResponsePaySubsModel
import com.example.domain.entity.ResponsePurchaseInfoModel
import com.example.domain.entity.ResponseSubsNeededModel
import com.example.domain.repository.PayRepository
import com.example.domain.repository.YelloRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val repository: YelloRepository,
    private val payRepository: PayRepository,
) : ViewModel() {

    var isFirstCreated: Boolean = true

    var currentInAppItem: String = ""

    private val _payCheck = MutableSharedFlow<UiState<Response>>()
    val payCheck: SharedFlow<UiState<Response>> = _payCheck.asSharedFlow()

    private val _postSubsCheckState = MutableLiveData<UiState<ResponsePaySubsModel?>>()
    val postSubsCheckState: LiveData<UiState<ResponsePaySubsModel?>> = _postSubsCheckState

    private val _postInAppCheckState = MutableLiveData<UiState<ResponsePayInAppModel?>>()
    val postInAppCheckState: LiveData<UiState<ResponsePayInAppModel?>> = _postInAppCheckState

    private val _getSubsNeededState = MutableLiveData<UiState<ResponseSubsNeededModel?>>()
    val getSubsNeededState: LiveData<UiState<ResponseSubsNeededModel?>> = _getSubsNeededState

    private val _getPurchaseInfoState = MutableLiveData<UiState<ResponsePurchaseInfoModel?>>()
    val getPurchaseInfoState: LiveData<UiState<ResponsePurchaseInfoModel?>> = _getPurchaseInfoState

    // 서버 통신 - (추후 삭제) amplitude 적용 전 클릭 수 수집
    fun payCheck(index: Int) {
        _payCheck.tryEmit(UiState.Loading)
        viewModelScope.launch {
            repository.payCheck(index)
                .onSuccess {
                    _payCheck.emit(UiState.Success(it))
                }.onFailure {
                    _payCheck.emit(UiState.Failure(""))
                }
        }
    }

    // 서버 통신 - 구독 상품 검증
    fun checkSubsToServer(request: RequestPayModel) {
        viewModelScope.launch {
            _postSubsCheckState.value = UiState.Loading
            runCatching {
                payRepository.postToCheckSubs(
                    request
                )
            }.onSuccess {
                _postSubsCheckState.value = UiState.Success(it)
            }.onFailure {
                _postSubsCheckState.value = UiState.Failure(it.message ?: "")
            }
        }
    }

    // 서버 통신 - 열람권 상품 검증
    fun checkInAppToServer(request: RequestPayModel) {
        viewModelScope.launch {
            _postInAppCheckState.value = UiState.Loading
            runCatching {
                payRepository.postToCheckInApp(
                    request
                )
            }.onSuccess {
                _postInAppCheckState.value = UiState.Success(it)
            }.onFailure {
                _postInAppCheckState.value = UiState.Failure(it.message ?: "")
            }
        }
    }

    // 서버 통신 - (아직 사용 X) 구독 재촉 알림 필요 여부 확인
    fun getSubsNeededFromServer() {
        viewModelScope.launch {
            runCatching {
                payRepository.getSubsNeeded()
            }.onSuccess {
                _getSubsNeededState.value = UiState.Success(it)
            }.onFailure {
                _getSubsNeededState.value = UiState.Failure(it.message ?: "")
            }
        }
    }

    // 서버 통신 - 구독 여부 & 열람권 개수 받아오기
    fun getPurchaseInfoFromServer() {
        viewModelScope.launch {
            runCatching {
                payRepository.getPurchaseInfo()
            }.onSuccess {
                _getPurchaseInfoState.value = UiState.Success(it)
            }.onFailure {
                _getPurchaseInfoState.value = UiState.Failure(it.message ?: "")
            }
        }
    }
}
