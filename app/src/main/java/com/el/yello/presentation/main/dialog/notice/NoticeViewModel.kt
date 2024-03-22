package com.el.yello.presentation.main.dialog.notice

import androidx.lifecycle.ViewModel
import com.el.yello.util.manager.AmplitudeManager
import com.example.domain.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
) : ViewModel() {
    private val _isNoticeDisabled = MutableStateFlow(false)
    val isNoticeDisabled
        get() = _isNoticeDisabled

    fun switchNoticeDisabledState() {
        _isNoticeDisabled.value = !isNoticeDisabled.value
    }

    fun setDisabledNotice(url: String) {
        if (isNoticeDisabled.value) {
            noticeRepository.setDisabledNoticeUrl(url)
            AmplitudeManager.trackEventWithProperties(EVENT_CLICK_POP_UP_NO)
        }
    }

    companion object {
        private const val EVENT_CLICK_POP_UP_NO = "click_notice_popup_no"
    }
}
