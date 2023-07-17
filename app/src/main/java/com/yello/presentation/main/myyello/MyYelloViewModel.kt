package com.yello.presentation.main.myyello

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.MyYello
import com.example.domain.repository.YelloRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyYelloViewModel @Inject constructor(
    private val repository: YelloRepository
) : ViewModel() {
    private val _myYelloData = MutableSharedFlow<UiState<MyYello>>()
    val myYelloData: SharedFlow<UiState<MyYello>> = _myYelloData.asSharedFlow()

    private val _totalCount = MutableStateFlow<Int>(0)
    val totalCount: StateFlow<Int> = _totalCount.asStateFlow()

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    fun getMyYelloList() {
        if (isPagingFinish) return
        _myYelloData.tryEmit(UiState.Loading)
        viewModelScope.launch {
            repository.getMyYelloList(++currentPage)
                .onSuccess {
                    totalPage = Math.ceil((it.totalCount * 0.1)).toInt()
                    if (totalPage == currentPage) isPagingFinish = true
                    _myYelloData.emit(
                        when {
                            it.yello.isEmpty() -> UiState.Empty
                            else -> UiState.Success(it)
                        }
                    )
                    _totalCount.value = it.totalCount
                }.onFailure {
                    _myYelloData.emit(UiState.Failure("옐로 리스트 서버 통신 실패"))
                }
        }
    }
}