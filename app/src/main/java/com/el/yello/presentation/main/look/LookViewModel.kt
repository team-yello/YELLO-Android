package com.el.yello.presentation.main.look

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.datasource.paging.LookPagingSource
import com.example.data.remote.service.LookService
import com.example.domain.entity.LookListModel.LookModel
import com.example.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LookViewModel @Inject constructor(
    private val lookService: LookService,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isFirstLoading = MutableStateFlow(true)
    val isFirstLoading: StateFlow<Boolean> = _isFirstLoading

    fun setFirstLoading(boolean: Boolean) {
        _isFirstLoading.value = boolean
    }

    fun getLookListWithPaging(): Flow<PagingData<LookModel>> =
        Pager(
            config = PagingConfig(LookPagingSource.LOOK_PAGE_SIZE),
            pagingSourceFactory = { LookPagingSource(lookService) }
        ).flow.cachedIn(viewModelScope)

    fun getYelloId() = authRepository.getYelloId()
}