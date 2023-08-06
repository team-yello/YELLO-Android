package com.el.yello.presentation.main.recommend.kakao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.main.recommend.list.RecommendViewHolder
import com.example.domain.entity.RecommendModel
import com.example.domain.entity.RequestRecommendKakaoModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.RecommendRepository
import com.example.ui.view.UiState
import com.kakao.sdk.talk.TalkApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class RecommendKakaoViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _getKakaoErrorResult = MutableLiveData<String>()
    val getKakaoErrorResult : LiveData<String> = _getKakaoErrorResult

    private val _postFriendsListState = MutableLiveData<UiState<RecommendModel?>>()
    val postFriendsListState: LiveData<UiState<RecommendModel?>> = _postFriendsListState

    private val _addFriendState = MutableLiveData<UiState<Unit>>()
    val addFriendState: LiveData<UiState<Unit>> = _addFriendState

    var itemPosition: Int? = null
    var itemHolder: RecommendViewHolder? = null

    private var currentOffset = -100
    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    fun setPositionAndHolder(position: Int, holder: RecommendViewHolder) {
        itemPosition = position
        itemHolder = holder
    }

    fun initPagingVariable() {
        currentOffset = -100
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
    }

    // 서버 통신 - 카카오 리스트 통신 후 친구 리스트 추가 서버 통신 진행
    fun addListWithKakaoIdList() {
        if (isPagingFinish) return
        currentOffset += 100
        currentPage += 1
        TalkApiClient.instance.friends(offset = currentOffset, limit = 100) { friends, error ->
            if (error != null) {
                _getKakaoErrorResult.value = error.message
            } else if (friends != null) {
                totalPage = ceil((friends.totalCount * 0.1)).toInt() - 1
                if (totalPage == currentPage) isPagingFinish = true
                val friendIdList: List<String> =
                    friends.elements?.map { friend -> friend.id.toString() } ?: listOf()
                getListFromServer(friendIdList)
            }
        }
    }



    // 서버 통신 - 추천 친구 리스트 추가
    private fun getListFromServer(friendKakaoId: List<String>) {
        viewModelScope.launch {
            _postFriendsListState.value = UiState.Loading
            runCatching {
                recommendRepository.postToGetKakaoFriendList(
                    0,
                    RequestRecommendKakaoModel(friendKakaoId),
                )
            }.onSuccess {
                it ?: return@launch
                _postFriendsListState.value = UiState.Success(it)
            }.onFailure {
                _postFriendsListState.value = UiState.Failure(it.message.toString())
            }
        }
    }

    // 서버 통신 - 친구 추가
    fun addFriendToServer(friendId: Long) {
        viewModelScope.launch {
            _addFriendState.value = UiState.Loading
            runCatching {
                recommendRepository.postFriendAdd(
                    friendId,
                )
            }.onSuccess {
                _addFriendState.value = UiState.Success(it)
            }.onFailure {
                _addFriendState.value = UiState.Failure(it.message.toString())
            }
        }
    }

    fun getYelloId() = authRepository.getYelloId()
}
