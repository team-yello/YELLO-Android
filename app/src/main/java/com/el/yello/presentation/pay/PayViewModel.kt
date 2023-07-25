package com.el.yello.presentation.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Response
import com.example.domain.repository.YelloRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PayViewModel @Inject constructor(
    private val repository: YelloRepository,
) : ViewModel() {
    private val _payCheck = MutableSharedFlow<UiState<Response>>()
    val payCheck: SharedFlow<UiState<Response>> = _payCheck.asSharedFlow()

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
}
