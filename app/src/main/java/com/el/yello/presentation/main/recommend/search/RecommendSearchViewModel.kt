package com.el.yello.presentation.main.recommend.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.main.recommend.list.RecommendViewHolder
import com.example.domain.repository.RecommendRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendSearchViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository
) : ViewModel() {

    private val _addFriendState = MutableLiveData<UiState<Unit>>()
    val addFriendState: LiveData<UiState<Unit>> = _addFriendState

    var itemPosition: Int? = null
    var itemHolder: RecommendSearchViewHolder? = null

    fun setPositionAndHolder(position: Int, holder: RecommendSearchViewHolder) {
        itemPosition = position
        itemHolder = holder
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
                _addFriendState.value = UiState.Failure(it.message ?: "")
            }
        }
    }
}
