package com.el.yello.presentation.main.myyello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.entity.MyYello
import com.example.domain.entity.vote.VoteCount
import com.example.domain.repository.YelloRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class MyYelloViewModel @Inject constructor(
    private val repository: YelloRepository,
) : ViewModel() {

    // Todo Flow 쓰면 상세보기 갔다 왔을 때 리스트 계속 관찰해서 임시로 LiveData로 구현
    private val _myYelloData = MutableLiveData<UiState<MyYello>>(UiState.Loading)
    val myYelloData: LiveData<UiState<MyYello>> = _myYelloData

    private val _totalCount = MutableStateFlow<Int>(0)
    val totalCount: StateFlow<Int> = _totalCount.asStateFlow()

    private val _voteCount = MutableStateFlow<UiState<VoteCount>>(UiState.Loading)
    val voteCount: StateFlow<UiState<VoteCount>> = _voteCount.asStateFlow()

    var position: Int = -1
        private set

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    fun setPosition(pos: Int) {
        position = pos
    }

    fun setToFirstPage() {
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
    }

    fun getMyYelloList() {
        if (isPagingFinish) return
        viewModelScope.launch {
            repository.getMyYelloList(++currentPage).onSuccess {
                    if (it == null) {
                        _myYelloData.value = UiState.Empty
                        return@launch
                    }
                    totalPage = ceil((it.totalCount * 0.1)).toInt() - 1
                    if (totalPage == currentPage) isPagingFinish = true
                    _myYelloData.value = when {
                        it.yello.isEmpty() -> UiState.Empty
                        else -> UiState.Success(it)
                    }
                    _totalCount.value = it.totalCount
                    setAmplitude(it)
                }.onFailure {
                    _myYelloData.value = UiState.Failure("내 쪽지 목록 서버 통신 실패")
                }
        }
    }

    fun getVoteCount() {
        viewModelScope.launch {
            repository.voteCount().onSuccess {
                    if (it != null) _voteCount.value = UiState.Success(it)
                }.onFailure {
                    _voteCount.value = UiState.Failure(it.message.toString())
                }
        }
    }

    private fun setAmplitude(myYello: MyYello) {
        AmplitudeUtils.updateUserIntProperties(
            "user_message_received",
            myYello.totalCount,
        )
        AmplitudeUtils.updateUserIntProperties(
            "user_message_open",
            myYello.openCount,
        )
        AmplitudeUtils.updateUserIntProperties(
            "user_message_open_keyword",
            myYello.openKeywordCount,
        )
        AmplitudeUtils.updateUserIntProperties(
            "user_message_open_firstletter",
            myYello.openNameCount,
        )
        AmplitudeUtils.updateUserIntProperties(
            "user_message_open_fullname",
            myYello.openFullNameCount,
        )
        AmplitudeUtils.updateUserIntProperties(
            "user_message_open_fullname",
            myYello.openFullNameCount,
        )
    }
}
