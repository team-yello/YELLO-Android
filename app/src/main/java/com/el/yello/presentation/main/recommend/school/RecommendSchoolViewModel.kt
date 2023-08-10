package com.el.yello.presentation.main.recommend.school

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.main.recommend.list.RecommendViewHolder
import com.example.domain.entity.RecommendModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.RecommendRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class RecommendSchoolViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _postFriendsListState = MutableLiveData<UiState<RecommendModel?>>()
    val postFriendsListState: LiveData<UiState<RecommendModel?>> = _postFriendsListState

    private val _addFriendState = MutableLiveData<UiState<Unit>>()
    val addFriendState: LiveData<UiState<Unit>> = _addFriendState

    var itemPosition: Int? = null
    var itemHolder: RecommendViewHolder? = null

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

    // 서버 통신 - 추천 친구 리스트 추가
    fun addListFromServer() {
        viewModelScope.launch {
            if (isPagingFinish) return@launch
            if (isFirstFriendsListPage) {
                _postFriendsListState.value = UiState.Loading
                isFirstFriendsListPage = false
            }
            runCatching {
                recommendRepository.getSchoolFriendList(
                    ++currentPage,
                )
            }.onSuccess {
                it ?: return@launch
                totalPage = ceil((it.totalCount * 0.1)).toInt() - 1
                if (totalPage == currentPage) isPagingFinish = true
                _postFriendsListState.value = UiState.Success(it)
            }.onFailure {
                _postFriendsListState.value = UiState.Failure(it.message ?: "")
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
                _addFriendState.value = UiState.Failure(it.message ?: "")
            }
        }
    }

    fun getYelloId() = authRepository.getYelloId()
}
