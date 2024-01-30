package com.el.yello.presentation.main.dialog.notice

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor() : ViewModel() {
    private val _isNoticeDisabled = MutableStateFlow(false)
    private val isNoticeDisabled
        get() = _isNoticeDisabled

    fun switchNoticeDisabledState(): Boolean {
        _isNoticeDisabled.value = !isNoticeDisabled.value
        return isNoticeDisabled.value
    }
}
