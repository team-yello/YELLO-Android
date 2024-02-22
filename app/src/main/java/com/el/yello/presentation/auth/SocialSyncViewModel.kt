package com.el.yello.presentation.auth

import androidx.lifecycle.ViewModel
import com.example.ui.state.UiState
import com.kakao.sdk.talk.TalkApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SocialSyncViewModel @Inject constructor(
) : ViewModel() {

    private val _getFriendListState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val getFriendListState: StateFlow<UiState<Unit>> = _getFriendListState

    fun getFriendsListFromKakao() {
        TalkApiClient.instance.friends { _, error ->
            _getFriendListState.value = UiState.Loading
            if (error == null) {
                _getFriendListState.value = UiState.Success(Unit)
            } else {
                _getFriendListState.value = UiState.Failure(error.message.toString())
            }
        }
    }
}