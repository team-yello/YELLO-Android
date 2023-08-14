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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LookViewModel @Inject constructor(
    private val lookService: LookService
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var isFirstLookListPage: Boolean = true

    fun setFirstPageLoading() {
        isFirstLookListPage = true
    }

    // 서버 통신 - 둘러보기 리스트 추가
    fun getLookList() = Pager(
        config = PagingConfig(10),
        pagingSourceFactory = { LookPagingSource(lookService) }
    ).flow.map { pagingData ->
        _isLoading.value = true
        pagingData
    }.cachedIn(viewModelScope)

    fun getTotalCount() {
        viewModelScope.launch {
            runCatching { lookService.getLookList(0) }
                .onSuccess {
                    _isLoading.value = false
                }
        }
    }

}
