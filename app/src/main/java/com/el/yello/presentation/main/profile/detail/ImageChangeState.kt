package com.el.yello.presentation.main.profile.detail

sealed class ImageChangeState {
    object Success : ImageChangeState()
    object NotChanged : ImageChangeState()
    object Error : ImageChangeState()
}