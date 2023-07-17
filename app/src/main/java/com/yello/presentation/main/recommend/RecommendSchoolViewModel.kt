package com.yello.presentation.main.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RecommendModel
import com.example.domain.repository.RecommendRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecommendSchoolViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository
) : ViewModel() {

    private val _postState = MutableLiveData<UiState<List<RecommendModel>>>()
    val postState: LiveData<UiState<List<RecommendModel>>> = _postState

    private val _addState = MutableLiveData<UiState<Unit>>()
    val addState: LiveData<UiState<Unit>> = _addState

    fun addListFromServer(page: Int) {

        viewModelScope.launch {
            _postState.value = UiState.Loading
            runCatching {
                recommendRepository.getSchoolFriendList(
                    page
                )
            }.onSuccess {
                _postState.value = UiState.Success(it)
            }.onFailure {
                _postState.value = UiState.Failure("학교 추천친구 리스트 서버 통신 실패")
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
                _addState.value = UiState.Failure("친구 추가 서버 통신 실패")
            }
        }
    }
}
