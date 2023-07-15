package com.yello.presentation.main.yello.vote

sealed class VoteState {
    object Success : VoteState()
    object InvalidSkip : VoteState()
    object InvalidCancel : VoteState()
    object InvalidShuffle : VoteState()
}
