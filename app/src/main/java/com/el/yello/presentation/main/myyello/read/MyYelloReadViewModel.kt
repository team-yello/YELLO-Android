package com.el.yello.presentation.main.myyello.read

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.CheckKeyword
import com.example.domain.entity.CheckName
import com.example.domain.entity.FullName
import com.example.domain.entity.YelloDetail
import com.example.domain.enum.PointEnum
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
    private val repository: YelloRepository,
) : ViewModel() {
    private val _yelloDetailData = MutableStateFlow<UiState<YelloDetail>>(UiState.Loading)
    val yelloDetailData: StateFlow<UiState<YelloDetail>> = _yelloDetailData.asStateFlow()

    private val _keywordData = MutableStateFlow<UiState<CheckKeyword>>(UiState.Loading)
    val keywordData: StateFlow<UiState<CheckKeyword>> = _keywordData.asStateFlow()

    private val _nameData = MutableStateFlow<UiState<CheckName>>(UiState.Loading)
    val nameData: StateFlow<UiState<CheckName>> = _nameData.asStateFlow()

    private val _isFinishCheck = MutableStateFlow(false)
    val isFinishCheck: StateFlow<Boolean> = _isFinishCheck.asStateFlow()

    private val _fullNameData = MutableStateFlow<UiState<FullName>>(UiState.Loading)
    val fullNameData: StateFlow<UiState<FullName>> = _fullNameData.asStateFlow()

    var pointType = 0
    var isTwoButton = false
    var myPoint = 0
        private set
    var myReadingTicketCount = 0
        private set

    private var voteId: Long = -1

    var nameIndex: Int? = null
        private set
    var isHintUsed: Boolean? = null
        private set

    var yelloDetail: YelloDetail? = null
        private set

    fun setNameIndex(index: Int) {
        nameIndex = index
    }

    fun setHintUsed(isUsed: Boolean) {
        isHintUsed = isUsed
    }

    fun setPointAndIsTwoButton(type: Int, isButton: Boolean) {
        pointType = type
        isTwoButton = isButton
    }

    fun setMyPoint(point: Int) {
        myPoint = point
    }

    private fun minusPoint(): Int {
        myPoint -= if (pointType == PointEnum.KEYWORD.ordinal) {
            100
        } else {
            300
        }
        return myPoint
    }

    private fun minusTicket() {
        myReadingTicketCount -= 1
    }

    fun setIsFinishCheck(isFinish: Boolean) {
        _isFinishCheck.value = isFinish
    }

    fun getYelloDetail(id: Long) {
        voteId = id
        viewModelScope.launch {
            repository.getYelloDetail(id)
                .onSuccess {
                    if (it == null) {
                        _yelloDetailData.value = UiState.Empty
                        return@launch
                    }
                    myReadingTicketCount = it.ticketCount
                    myPoint = it.currentPoint
                    yelloDetail = it
                    _yelloDetailData.value = UiState.Success(it)
                }.onFailure {
                    _yelloDetailData.value = UiState.Failure("옐로 상세보기 서버 통신 실패")
                }
        }
    }

    fun checkKeyword() {
        viewModelScope.launch {
            repository.checkKeyword(voteId)
                .onSuccess {
                    if (it == null) {
                        _keywordData.value = UiState.Empty
                    } else {
                        minusPoint()
                        _keywordData.value = UiState.Success(it)
                    }
                }.onFailure {
                    _keywordData.value = UiState.Failure("키워드 확인 서버 통신 실패")
                }
        }
    }

    fun checkInitial() {
        viewModelScope.launch {
            repository.checkName(voteId)
                .onSuccess {
                    if (it == null) {
                        _nameData.value = UiState.Empty
                    } else {
                        if (pointType == PointEnum.INITIAL.ordinal) {
                            minusPoint()
                        }
                        _nameData.value = UiState.Success(it)

                    }
                }.onFailure {
                    _nameData.value = UiState.Failure("이름 확인 서버 통신 실패")
                }
        }
    }

    fun postFullName() {
        viewModelScope.launch {
            repository.postFullName(voteId)
                .onSuccess {
                    if (it == null) {
                        _fullNameData.value = UiState.Empty
                    } else {
                        minusTicket()
                        _fullNameData.value = UiState.Success(it)
                    }
                }.onFailure {
                    _fullNameData.value = UiState.Failure("열람권 사용하기 서버 통신 실패")
                }
        }
    }
}
