package com.el.yello.presentation.main.look

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.LookListModel
import com.example.domain.repository.LookRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LookViewModel @Inject constructor(
    private val lookRepository: LookRepository
) : ViewModel() {

    private val _getLookListState = MutableLiveData<UiState<LookListModel?>>()
    val getLookListState: LiveData<UiState<LookListModel?>> = _getLookListState

    private var isFirstLookListPage: Boolean = true

    fun setFirstPageLoading() {
        isFirstLookListPage = true
    }

    // 서버 통신 - 둘러보기 리스트 추가
    fun addLookListFromServer(page: Int) {
        if (isFirstLookListPage) {
            _getLookListState.value = UiState.Loading
            isFirstLookListPage = false
        }
        viewModelScope.launch {
            runCatching {
                lookRepository.getLookList(
                    page
                )
            }.onSuccess {
                it ?: return@launch
                _getLookListState.value = UiState.Success(it)
            }.onFailure {
                _getLookListState.value = UiState.Failure(it.message ?: "")
            }
        }
    }
}
