package com.yello.presentation.main.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RecommendModel
import com.example.domain.entity.RequestRecommendKakaoModel
import com.example.domain.repository.RecommendRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendKakaoViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository
) : ViewModel() {

    private val _postState = MutableLiveData<UiState<List<RecommendModel>>>()
    val postState: LiveData<UiState<List<RecommendModel>>> = _postState

    fun addListFromServer(accessToken: String, page: Int, friendKakaoId: List<String>) {

        viewModelScope.launch {
            _postState.value = UiState.Loading
            runCatching {
                recommendRepository.postToGetKakaoFriendList(
                    accessToken, page, RequestRecommendKakaoModel(friendKakaoId)
                )
            }.onSuccess {
                _postState.value = UiState.Success(it)
            }.onFailure {
                _postState.value = UiState.Failure("카카오 추천친구 리스트 서버 통신 실패")
            }
        }
    }
}