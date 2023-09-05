package com.el.yello.presentation.main.recommend.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RecommendSearchModel
import com.example.domain.repository.RecommendRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class RecommendSearchViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository
) : ViewModel() {

    private val _postFriendsListState = MutableLiveData<UiState<RecommendSearchModel?>>()
    val postFriendsListState: LiveData<UiState<RecommendSearchModel?>> = _postFriendsListState

    private val _addFriendState = MutableLiveData<UiState<Unit>>()
    val addFriendState: LiveData<UiState<Unit>> = _addFriendState

    var itemPosition: Int? = null
    var itemHolder: RecommendSearchViewHolder? = null

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    var isNewText = true

    fun setPositionAndHolder(position: Int, holder: RecommendSearchViewHolder) {
        itemPosition = position
        itemHolder = holder
    }

    fun setNewPage() {
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
    }

    // 서버 통신 - 추천 친구 리스트 추가
    fun setListFromServer(keyword: String) {
        if (isPagingFinish) return
        _postFriendsListState.value = UiState.Loading
        viewModelScope.launch {
            recommendRepository.getSearchList(
                ++currentPage,
                keyword
            )
                .onSuccess {
                    it ?: return@launch
                    totalPage = ceil((it.totalCount * 0.1)).toInt() - 1
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
}
