package com.el.yello.presentation.main.look

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.LookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LookViewModel @Inject constructor(
    private val lookRepository: LookRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _getErrorResult = MutableLiveData<String>()
    val getErrorResult: LiveData<String> = _getErrorResult

    fun setNotLoading() {
        _isLoading.value = false
    }

    // 서버 통신 - 둘러보기 리스트 추가
    fun getLookListWithPaging() {
        viewModelScope.launch {
            lookRepository.getLookList().cachedIn(viewModelScope)
                .onStart {
                    _isLoading.value = true
                    checkLookConnection()
                }
        }
    }

    // 서버 통신 여부 - 페이지 한번 가져와 보는거로 확인
    private fun checkLookConnection() {
        viewModelScope.launch {
            runCatching {
                lookRepository.getLookList()
            }
                .onSuccess {
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _isLoading.value = false
                    _getErrorResult.value = exception.message
                }
        }
    }

    fun getYelloId() = authRepository.getYelloId()
}