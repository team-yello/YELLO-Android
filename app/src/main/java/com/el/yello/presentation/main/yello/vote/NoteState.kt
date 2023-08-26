package com.el.yello.presentation.main.yello.vote

sealed class NoteState {
    object Success : NoteState()
    object InvalidCancel : NoteState()
    object InvalidShuffle : NoteState()
    object Failure : NoteState()
}
