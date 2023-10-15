package com.el.yello.presentation.main.recommend.school

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.main.recommend.list.RecommendViewHolder
import com.example.domain.entity.RecommendListModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.RecommendRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class RecommendSchoolViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _postFriendsListState = MutableStateFlow<UiState<RecommendListModel>>(UiState.Empty)
    val postFriendsListState: StateFlow<UiState<RecommendListModel>> = _postFriendsListState

    private val _addFriendState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val addFriendState: StateFlow<UiState<Unit>> = _addFriendState

    var isSearchViewShowed = false

    var itemPosition: Int? = null
    var itemHolder: RecommendViewHolder? = null

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    private var isFirstFriendsListPage: Boolean = true

    fun setFirstPageLoading() {
        isFirstFriendsListPage = true
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
        _postFriendsListState.value = UiState.Empty
        _addFriendState.value = UiState.Empty
    }

    fun setPositionAndHolder(position: Int, holder: RecommendViewHolder) {
        itemPosition = position
        itemHolder = holder
    }

    // 서버 통신 - 추천 친구 리스트 추가
    fun addListFromServer() {
        viewModelScope.launch {
            if (isPagingFinish) return@launch
            if (isFirstFriendsListPage) {
                _postFriendsListState.value = UiState.Loading
                isFirstFriendsListPage = false
            }
            recommendRepository.getSchoolFriendList(
                ++currentPage,
            )
                .onSuccess {
                    it ?: return@launch
                    totalPage = ceil((it.totalCount * 0.01)).toInt() - 1
                    if (totalPage == currentPage) isPagingFinish = true
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
