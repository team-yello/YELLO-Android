package com.el.yello.presentation.main.look

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.domain.entity.LookListModel.LookModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.LookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LookViewModel @Inject constructor(
    private val lookRepository: LookRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isFirstLoading = MutableStateFlow(false)
    val isFirstLoading: StateFlow<Boolean> = _isFirstLoading.asStateFlow()

    fun setFirstLoading(boolean: Boolean) {
        _isFirstLoading.value = boolean
    }

    fun getLookListWithPaging(): Flow<PagingData<LookModel>> {
        return lookRepository.getLookList()
    }

    fun getYelloId() = authRepository.getYelloId()
}