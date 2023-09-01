package com.el.yello.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ui.view.UiState
import com.kakao.sdk.talk.TalkApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SocialSyncViewModel @Inject constructor(
) : ViewModel() {

    private val _getFriendListState = MutableLiveData<UiState<Unit>>()
    val getFriendListState: LiveData<UiState<Unit>> = _getFriendListState

    // 카카오 통신 - 카카오 친구목록 동의 받기
    fun getKakaoFriendsList() {
        TalkApiClient.instance.friends { _, error ->
            _getFriendListState.value = UiState.Loading
            if (error == null) {
                _getFriendListState.value = UiState.Success(Unit)
            } else {
                _getFriendListState.value = UiState.Failure(error.message ?: "")
            }
        }
    }
}