package com.yello.presentation.main.myyello.read

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.YelloDetail
import com.example.domain.repository.YelloRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyYelloReadViewModel @Inject constructor(
    private val repository: YelloRepository
) : ViewModel() {
    private val _yelloDetailData = MutableStateFlow<UiState<YelloDetail>>(UiState.Loading)
    val yelloDetailData: StateFlow<UiState<YelloDetail>> = _yelloDetailData.asStateFlow()

    fun getYelloDetail(id: Long) {
        viewModelScope.launch {
            repository.getYelloDetail(id)
                .onSuccess {
                    _yelloDetailData.value = UiState.Success(it)
                }.onFailure {
                    _yelloDetailData.value = UiState.Failure("옐로 상세보기 서버 통신 실패")
                }
        }
    }
}