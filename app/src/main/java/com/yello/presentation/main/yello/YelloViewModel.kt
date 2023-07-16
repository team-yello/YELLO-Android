package com.yello.presentation.main.yello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YelloViewModel @Inject constructor() : ViewModel() {
    private val _leftTime = MutableLiveData(SEC_MAX_LOCK_TIME)
    val leftTime: LiveData<Int> = _leftTime

    fun decreaseTime() {
        viewModelScope.launch {
            leftTime.value ?: return@launch
            while (requireNotNull(leftTime.value) > 0) {
                delay(1000L)
                if (requireNotNull(leftTime.value) <= 0) return@launch
                _leftTime.value = leftTime.value?.minus(1)
            }
        }

        // TODO: Yello State 바꾸기
    }

    companion object {
        private const val SEC_MAX_LOCK_TIME = 4000
    }
}
