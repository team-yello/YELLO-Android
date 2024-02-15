package com.el.yello.presentation.main.myyello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.entity.MyYello
import com.example.domain.entity.notice.Banner
import com.example.domain.entity.vote.VoteCount
import com.example.domain.repository.NoticeRepository
import com.example.domain.repository.YelloRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class MyYelloViewModel @Inject constructor(
    private val yelloRepository: YelloRepository,
    private val noticeRepository: NoticeRepository,
) : ViewModel() {

    // Todo Flow 쓰면 상세 보기 갔다 왔을 때 리스트 계속 관찰해서 임시로 LiveData로 구현
    private val _myYelloData = MutableLiveData<UiState<MyYello>>(UiState.Loading)
    val myYelloData: LiveData<UiState<MyYello>> = _myYelloData

    private val _totalCount = MutableStateFlow<Int>(0)
    val totalCount: StateFlow<Int> = _totalCount.asStateFlow()

    private val _voteCount = MutableStateFlow<UiState<VoteCount>>(UiState.Loading)
    val voteCount: StateFlow<UiState<VoteCount>> = _voteCount.asStateFlow()

    private val _getBannerState = MutableStateFlow<UiState<Banner>>(UiState.Loading)
    val getBannerState: StateFlow<UiState<Banner>> = _getBannerState.asStateFlow()

    var isFirstLoading = true

    var position: Int = -1
        private set

    var readCount: Int = 0
        private set

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    init {
        getMyYelloList()
        getBanner()
        resetReadCount()
    }

    fun setPosition(pos: Int) {
        position = pos
    }

    private fun resetReadCount() {
        readCount = 0
    }

    fun addReadCount() {
        readCount += 1
    }

    fun setToFirstPage() {
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
        isFirstLoading = true
    }

    fun getMyYelloList() {
        if (isPagingFinish) return
        viewModelScope.launch {
            yelloRepository.getMyYelloList(++currentPage)
                .onSuccess {
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
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        _myYelloData.value = UiState.Failure(t.code().toString())
                    }
                }
        }
    }

    fun getVoteCount() {
        viewModelScope.launch {
            yelloRepository.voteCount()
                .onSuccess {
                    if (it != null) _voteCount.value = UiState.Success(it)
                }
                .onFailure {
                    _voteCount.value = UiState.Failure(it.message.toString())
                }
        }
    }

    private fun getBanner() {
        viewModelScope.launch {
            noticeRepository.getBanner()
                .onSuccess { banner ->
                    if (banner == null) {
                        _getBannerState.value = UiState.Empty
                        return@onSuccess
                    }

                    _getBannerState.value = UiState.Success(banner)
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        _getBannerState.value = UiState.Failure(t.code().toString())
                    }
                }
        }
    }

    private fun setAmplitude(myYello: MyYello) {
        AmplitudeUtils.updateUserIntProperties(
            NAME_USER_MESSAGE_RECEIVED,
            myYello.totalCount,
        )
        AmplitudeUtils.updateUserIntProperties(
            NAME_USER_MESSAGE_OPEN,
            myYello.openCount,
        )
        AmplitudeUtils.updateUserIntProperties(
            NAME_USER_MESSAGE_OPEN_KEYWORD,
            myYello.openKeywordCount,
        )
        AmplitudeUtils.updateUserIntProperties(
            NAME_USER_MESSAGE_OPEN_FIRST_LETTER,
            myYello.openNameCount,
        )
        AmplitudeUtils.updateUserIntProperties(
            NAME_USER_MESSAGE_OPEN_FULL_NAME,
            myYello.openFullNameCount,
        )
        AmplitudeUtils.updateUserIntProperties(
            NAME_USER_MESSAGE_OPEN_FULL_NAME,
            myYello.openFullNameCount,
        )
    }

    companion object {
        private const val NAME_USER_MESSAGE_RECEIVED = "user_message_received"
        private const val NAME_USER_MESSAGE_OPEN = "user_message_open"
        private const val NAME_USER_MESSAGE_OPEN_KEYWORD = "user_message_open_keyword"
        private const val NAME_USER_MESSAGE_OPEN_FIRST_LETTER = "user_message_open_first_letter"
        private const val NAME_USER_MESSAGE_OPEN_FULL_NAME = "user_message_open_fullname"
    }
}
