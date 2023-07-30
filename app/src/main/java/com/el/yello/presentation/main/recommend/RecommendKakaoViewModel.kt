package com.el.yello.presentation.main.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RecommendModel
import com.example.domain.entity.RequestRecommendKakaoModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.RecommendRepository
import com.example.ui.view.UiState
import com.kakao.sdk.talk.TalkApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class RecommendKakaoViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _postState = MutableLiveData<UiState<RecommendModel?>>()
    val postState: LiveData<UiState<RecommendModel?>> = _postState

    private val _addState = MutableLiveData<UiState<Unit>>()
    val addState: LiveData<UiState<Unit>> = _addState

    var itemPosition: Int? = null
    var itemHolder: RecommendViewHolder? = null

    private var currentOffset = -10
    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    fun setPositionAndHolder(position: Int, holder: RecommendViewHolder) {
        itemPosition = position
        itemHolder = holder
    }

    fun initPagingVariable() {
        currentOffset = -10
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
    }

    // 서버 통신 - 카카오 리스트 통신 후 친구 리스트 추가 서버 통신 진행
    fun addListWithKakaoIdList() {
        if (isPagingFinish) return
        currentOffset += 10
        currentPage += 1
        TalkApiClient.instance.friends(offset = currentOffset, limit = 10) { friends, error ->
            if (error != null) {
                Timber.e(error, "카카오톡 친구목록 가져오기 실패")
            } else if (friends != null) {
                totalPage = ceil((friends.totalCount * 0.1)).toInt() - 1
                if (totalPage == currentPage) isPagingFinish = true
                val friendIdList: List<String> =
                    friends.elements?.map { friend -> friend.id.toString() } ?: listOf()
                getListFromServer(friendIdList)
            } else {
                Timber.d("연동 가능한 카카오톡 친구 없음")
            }
        }
    }

    // 서버 통신 - 추천 친구 리스트 추가
    private fun getListFromServer(friendKakaoId: List<String>) {
        viewModelScope.launch {
            _postState.value = UiState.Loading
            runCatching {
                recommendRepository.postToGetKakaoFriendList(
                    0,
                    RequestRecommendKakaoModel(friendKakaoId),
                )
            }.onSuccess {
                it ?: return@launch
                _postState.value = UiState.Success(it)
                if (currentPage < 3 && !isPagingFinish) addListWithKakaoIdList()
            }.onFailure {
                _postState.value = UiState.Failure(it.message.toString())
            }
        }
    }

    // 서버 통신 - 친구 추가
    fun addFriendToServer(friendId: Long) {
        viewModelScope.launch {
            _addState.value = UiState.Loading
            runCatching {
                recommendRepository.postFriendAdd(
                    friendId,
                )
            }.onSuccess {
                _addState.value = UiState.Success(it)
            }.onFailure {
                _addState.value = UiState.Failure(it.message.toString())
            }
        }
    }

    fun getYelloId() = authRepository.getYelloId() ?: ""
}
