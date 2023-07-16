package com.yello.presentation.main.yello.vote

sealed class NoteState {
    object Success : NoteState()
    object InvalidSkip : NoteState()
    object InvalidCancel : NoteState()
    object InvalidShuffle : NoteState()
    object Failure : NoteState()
}
