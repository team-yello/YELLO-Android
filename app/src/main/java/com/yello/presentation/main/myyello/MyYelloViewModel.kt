package com.yello.presentation.main.myyello

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.MyYello
import com.example.domain.repository.YelloRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class MyYelloViewModel @Inject constructor(
    private val repository: YelloRepository
) : ViewModel() {
    private val _myYelloData = MutableStateFlow<UiState<MyYello>>(UiState.Loading)
    val myYelloData: SharedFlow<UiState<MyYello>> = _myYelloData.asSharedFlow()

    private var currentPage = 0
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    fun getMyYelloList() {
        if (isPagingFinish) return
        _myYelloData.value = UiState.Loading
        viewModelScope.launch {
            repository.getMyYelloList(++currentPage)
                .onSuccess {
                    it ?: return@launch
                    totalPage = ceil((it.totalCount * 0.1)).toInt()
                    if (totalPage == currentPage) isPagingFinish = true
                    _myYelloData.value = when {
                        it.yello.isEmpty() -> UiState.Empty
                        else -> UiState.Success(it)
                    }

                }.onFailure {
                    _myYelloData.emit(UiState.Failure("옐로 리스트 서버 통신 실패"))
                }
        }
    }
}