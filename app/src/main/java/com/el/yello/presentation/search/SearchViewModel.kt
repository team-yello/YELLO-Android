package com.el.yello.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.SearchListModel
import com.example.domain.repository.RecommendRepository
import com.example.domain.repository.SearchRepository
import com.example.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _postFriendsListState = MutableStateFlow<UiState<SearchListModel>>(UiState.Empty)
    val postFriendsListState: StateFlow<UiState<SearchListModel>> = _postFriendsListState

    private val _addFriendState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val addFriendState: StateFlow<UiState<Unit>> = _addFriendState

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    var isFirstLoading = true

    var itemPosition: Int? = null
    var itemHolder: SearchViewHolder? = null

    fun setPositionAndHolder(position: Int, holder: SearchViewHolder) {
        itemPosition = position
        itemHolder = holder
    }

    fun setNewPage() {
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
        isFirstLoading = true
        _postFriendsListState.value = UiState.Empty
    }

    fun setFriendsListFromServer(keyword: String) {
        if (isPagingFinish) return
        viewModelScope.launch {
            searchRepository.getSearchList(
                ++currentPage,
                keyword
            )
                .onSuccess {
                    it ?: return@launch
                    totalPage = ceil((it.totalCount * 0.1)).toInt() - 1
                    if (totalPage == currentPage) isPagingFinish = true
                    _postFriendsListState.value = UiState.Success(it)
                }
                .onFailure { t ->
                    _postFriendsListState.value = UiState.Failure(t.message.toString())
                }
        }
    }

    fun addFriendToServer(friendId: Long) {
        _addFriendState.value = UiState.Loading
        viewModelScope.launch {
            recommendRepository.postFriendAdd(friendId)
                .onSuccess {
                    _addFriendState.value = UiState.Success(it)
                }
                .onFailure { t ->
                    _addFriendState.value = UiState.Failure(t.message.toString())
                }
        }
    }
}
