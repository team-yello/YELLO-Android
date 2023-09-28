package com.el.yello.presentation.main.recommend.kakao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.main.recommend.list.RecommendViewHolder
import com.example.domain.entity.RecommendListModel
import com.example.domain.entity.RecommendRequestModel
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
    val getKakaoErrorResult: LiveData<String> = _getKakaoErrorResult

    private val _postFriendsListState = MutableLiveData<UiState<RecommendListModel?>>()
    val postFriendsListState: LiveData<UiState<RecommendListModel?>> = _postFriendsListState

    private val _addFriendState = MutableLiveData<UiState<Unit>>()
    val addFriendState: LiveData<UiState<Unit>> = _addFriendState

    private val _isSearchViewShowed = MutableLiveData(false)
    val isSearchViewShowed: LiveData<Boolean> = _isSearchViewShowed

    var itemPosition: Int? = null
    var itemHolder: RecommendViewHolder? = null

    private var currentOffset = -100
    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    private var isFirstFriendsListPage: Boolean = true

    fun setFirstPageLoading() {
        isFirstFriendsListPage = true
    }

    fun setPositionAndHolder(position: Int, holder: RecommendViewHolder) {
        itemPosition = position
        itemHolder = holder
    }

    fun updateIsSearchViewShowed(newValue: Boolean) {
        _isSearchViewShowed.value = newValue
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
        if (isFirstFriendsListPage) {
            _postFriendsListState.value = UiState.Loading
            isFirstFriendsListPage = false
        }
        TalkApiClient.instance.friends(offset = currentOffset, limit = 100) { friends, error ->
            if (error != null) {
                _getKakaoErrorResult.value = error.message
            } else if (friends != null) {
                totalPage = ceil((friends.totalCount * 0.01)).toInt() - 1
                if (totalPage == currentPage) isPagingFinish = true
                getListFromServer(
                    friends.elements?.map { friend -> friend.id.toString() } ?: listOf()
                )
            }
        }
    }

    // 서버 통신 - 추천 친구 리스트 추가
    private fun getListFromServer(friendsKakaoId: List<String>) {
        viewModelScope.launch {
            recommendRepository.postToGetKakaoFriendList(
                0,
                RecommendRequestModel(friendsKakaoId),
            )
                .onSuccess {
                    it ?: return@launch
                    _postFriendsListState.value = UiState.Success(it)
                }
                .onFailure {
                    _postFriendsListState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    // 서버 통신 - 친구 추가
    fun addFriendToServer(friendId: Long) {
        viewModelScope.launch {
            _addFriendState.value = UiState.Loading
            recommendRepository.postFriendAdd(friendId)
                .onSuccess {
                    _addFriendState.value = UiState.Success(it)
                }
                .onFailure {
                    _addFriendState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    fun getYelloId() = authRepository.getYelloId()
}
