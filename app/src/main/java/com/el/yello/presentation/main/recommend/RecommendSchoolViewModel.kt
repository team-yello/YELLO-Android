package com.el.yello.presentation.main.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RecommendModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.RecommendRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.ceil
import kotlinx.coroutines.launch

@HiltViewModel
class RecommendSchoolViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _postState = MutableLiveData<UiState<RecommendModel?>>()
    val postState: LiveData<UiState<RecommendModel?>> = _postState

    private val _addState = MutableLiveData<UiState<Unit>>()
    val addState: LiveData<UiState<Unit>> = _addState

    var itemPosition: Int? = null
    var itemHolder: RecommendViewHolder? = null

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    fun setPositionAndHolder(position: Int, holder: RecommendViewHolder) {
        itemPosition = position
        itemHolder = holder
    }

    // 서버 통신 - 추천 친구 리스트 추가
    fun addListFromServer() {
        viewModelScope.launch {
            if (isPagingFinish) return@launch
            _postState.value = UiState.Loading
            runCatching {
                recommendRepository.getSchoolFriendList(
                    ++currentPage,
                )
            }.onSuccess {
                it ?: return@launch
                totalPage = ceil((it.totalCount * 0.1)).toInt() - 1
                if (totalPage == currentPage) isPagingFinish = true
                _postState.value = UiState.Success(it)
            }.onFailure {
                _postState.value = UiState.Failure(it.message ?: "")
            }
        }
    }

    // 서버 통신 -친구 추가
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
                _addState.value = UiState.Failure(it.message ?: "")
            }
        }
    }

    fun getYelloId() = authRepository.getYelloId() ?: ""
}
