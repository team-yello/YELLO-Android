package com.el.yello.presentation.main.look

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.ResponseLookListModel.LookModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.LookRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LookViewModel @Inject constructor(
    private val lookRepository: LookRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _getLookListState = MutableStateFlow<UiState<PagingData<LookModel>>>(UiState.Empty)
    val getLookListState: StateFlow<UiState<PagingData<LookModel>>> = _getLookListState.asStateFlow()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun setNotLoading() {
        _isLoading.value = false
    }

    // 서버 통신 - 둘러보기 리스트 추가
    fun getLookListWithPaging() {
        viewModelScope.launch {
            _getLookListState.emit(UiState.Loading)
            try {
                lookRepository.getLookList().cachedIn(viewModelScope)
                    .onStart {
                        _isLoading.value = true
                    }
                    .collectLatest { response ->
                        _getLookListState.emit(UiState.Success(response))
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _getLookListState.emit(UiState.Failure(e.message.toString()))
            }
        }
    }

    fun getYelloId() = authRepository.getYelloId()
}