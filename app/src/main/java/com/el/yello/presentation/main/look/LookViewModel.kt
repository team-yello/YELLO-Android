package com.el.yello.presentation.main.look

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.data.datasource.remote.LookPagingSource
import com.example.data.remote.service.LookService
import com.example.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LookViewModel @Inject constructor(
    private val lookService: LookService,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _getErrorResult = MutableLiveData<String>()
    val getErrorResult: LiveData<String> = _getErrorResult

    fun setNotLoading() {
        _isLoading.value = false
    }

    fun setPagingLoading() {
        _isLoading.value = true
    }

    // 서버 통신 - 둘러보기 리스트 추가
    fun getLookListWithPaging() = Pager(
        config = PagingConfig(10),
        pagingSourceFactory = { LookPagingSource(lookService) }
    ).flow.cachedIn(viewModelScope).onStart {
        setPagingLoading()
        checkLookConnection()
    }

    // 서버 통신 확인
    private fun checkLookConnection() {
        viewModelScope.launch {
            runCatching {
                lookService.getLookList(0)
            }.onSuccess {
                _isLoading.value = false
            }.onFailure { exception ->
                _isLoading.value = false
                _getErrorResult.value = exception.message
            }
        }
    }

    fun getYelloId() = authRepository.getYelloId()
}