package com.el.yello.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.domain.entity.SearchListModel.SearchFriendModel
import com.example.domain.repository.RecommendRepository
import com.example.domain.repository.SearchRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _isFirstLoading = MutableStateFlow(false)
    val isFirstLoading: StateFlow<Boolean> = _isFirstLoading.asStateFlow()

    private val _addFriendState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val addFriendState: StateFlow<UiState<Unit>> = _addFriendState.asStateFlow()

    var itemPosition: Int? = null
    var itemHolder: SearchViewHolder? = null

    fun setPositionAndHolder(position: Int, holder: SearchViewHolder) {
        itemPosition = position
        itemHolder = holder
    }

    fun setFirstLoading(boolean: Boolean) {
        _isFirstLoading.value = boolean
    }

    // 서버 통신 - 추천 친구 리스트 추가
    fun setListFromServer(keyword: String): Flow<PagingData<SearchFriendModel>> {
        return searchRepository.getSearchList(keyword)
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
