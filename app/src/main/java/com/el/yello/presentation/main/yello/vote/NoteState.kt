package com.el.yello.presentation.main.yello.vote

sealed class NoteState {
    object Success : NoteState()
    object InvalidSkip : NoteState()
    object InvalidCancel : NoteState()
    object InvalidShuffle : NoteState()
    object InvalidName : NoteState()
    object Failure : NoteState()
}
