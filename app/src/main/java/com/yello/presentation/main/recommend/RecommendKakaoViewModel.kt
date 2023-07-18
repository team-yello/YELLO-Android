package com.yello.presentation.main.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RecommendAddModel
import com.example.domain.entity.RecommendModel
import com.example.domain.entity.RequestRecommendKakaoModel
import com.example.domain.repository.RecommendRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class RecommendKakaoViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository
) : ViewModel() {

    private val _postState = MutableLiveData<UiState<RecommendModel?>>()
    val postState: LiveData<UiState<RecommendModel?>> = _postState

    private val _addState = MutableLiveData<UiState<RecommendAddModel>>()
    val addState: LiveData<UiState<RecommendAddModel>> = _addState

    var itemPosition: Int? = null
    var itemHolder: RecommendViewHolder? = null

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    fun setPositionAndHolder(position: Int, holder: RecommendViewHolder) {
        itemPosition = position
        itemHolder = holder
    }

    fun addListFromServer(friendKakaoId: List<String>) {

        viewModelScope.launch {
            runCatching {
                recommendRepository.postToGetKakaoFriendList(
                    ++currentPage,
                    RequestRecommendKakaoModel(friendKakaoId)
                )
            }.onSuccess {
                it ?: return@launch
                totalPage = ceil((it.totalCount * 0.1)).toInt()
                if (totalPage == currentPage) isPagingFinish = true
                _postState.value = UiState.Success(it)
            }.onFailure {
                _postState.value = UiState.Failure(it.message ?: "")
            }
        }
    }

    fun addFriendToServer(friendId: Long) {

        viewModelScope.launch {
            _addState.value = UiState.Loading
            runCatching {
                recommendRepository.postFriendAdd(
                    friendId
                )
            }.onSuccess {
                _addState.value = UiState.Success(it)
            }.onFailure {
                _addState.value = UiState.Failure(it.message ?: "")
            }
        }
    }
}