package com.yello.presentation.yello

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.CheckKeyword
import com.example.domain.entity.CheckName
import com.example.domain.entity.MyYello
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
class YelloViewModel @Inject constructor(
    private val repository: YelloRepository
) : ViewModel() {
    private val _myYelloData = MutableStateFlow<UiState<MyYello>>(UiState.Loading)
    val myYelloData: StateFlow<UiState<MyYello>> = _myYelloData.asStateFlow()

    private val _yelloDetailData = MutableStateFlow<UiState<YelloDetail>>(UiState.Loading)
    val yelloDetailData: StateFlow<UiState<YelloDetail>> = _yelloDetailData.asStateFlow()

    private val _keywordData = MutableStateFlow<UiState<CheckKeyword>>(UiState.Loading)
    val keywordData: StateFlow<UiState<CheckKeyword>> = _keywordData.asStateFlow()

    private val _nameData = MutableStateFlow<UiState<CheckName>>(UiState.Loading)
    val nameData: StateFlow<UiState<CheckName>> = _nameData.asStateFlow()

    fun getMyYelloList() {
        viewModelScope.launch {
            repository.getMyYelloList(1)
                .onSuccess {
                    _myYelloData.value = UiState.Success(it)
                }.onFailure {
                    _myYelloData.value = UiState.Failure("옐로 리스트 서버 통신 실패")
                }
        }
    }

    fun getYelloDetail() {
        viewModelScope.launch {
            repository.getYelloDetail(4L)
                .onSuccess {
                    _yelloDetailData.value = UiState.Success(it)
                }.onFailure {
                    _yelloDetailData.value = UiState.Failure("옐로 상세보기 서버 통신 실패")
                }
        }
    }

    fun checkKeyword() {
        viewModelScope.launch {
            repository.checkKeyword(4L)
                .onSuccess {
                    _keywordData.value = UiState.Success(it)
                }.onFailure {
                    _keywordData.value = UiState.Failure("키워드 확인 서버 통신 실패")
                }
        }
    }

    fun checkName() {
        viewModelScope.launch {
            repository.checkName(4L)
                .onSuccess {
                    _nameData.value = UiState.Success(it)
                }.onFailure {
                    _nameData.value = UiState.Failure("이름 확인 서버 통신 실패")
                }
        }
    }
}