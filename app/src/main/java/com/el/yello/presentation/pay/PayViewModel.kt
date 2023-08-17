package com.el.yello.presentation.pay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RequestPayModel
import com.example.domain.entity.Response
import com.example.domain.entity.ResponsePaySubsModel
import com.example.domain.entity.ResponsePayInAppModel
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

    var isFirstCreated : Boolean = true

    var currentInAppItem: Int = 0

    private val _payCheck = MutableSharedFlow<UiState<Response>>()
    val payCheck: SharedFlow<UiState<Response>> = _payCheck.asSharedFlow()

    private val _postSubsCheckState = MutableLiveData<UiState<ResponsePaySubsModel?>>()
    val postSubsCheckState: LiveData<UiState<ResponsePaySubsModel?>> = _postSubsCheckState

    private val _postInAppCheckState = MutableLiveData<UiState<ResponsePayInAppModel?>>()
    val postInAppCheckState: LiveData<UiState<ResponsePayInAppModel?>> = _postInAppCheckState

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
}
