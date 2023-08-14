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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class LookViewModel @Inject constructor(
    private val lookService: LookService
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun setFirstPageLoading() {
        _isLoading.value = false

    }

    // 서버 통신 - 둘러보기 리스트 추가
    fun getLookListWithPaging() = Pager(
        config = PagingConfig(10),
        pagingSourceFactory = { LookPagingSource(lookService) }
    ).flow.map { pagingData ->
        _isLoading.value = false
        pagingData
    }.cachedIn(viewModelScope).onStart {
        _isLoading.value = true
    }
}