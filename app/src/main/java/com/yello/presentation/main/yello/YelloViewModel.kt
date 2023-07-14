package com.yello.presentation.main.yello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class YelloViewModel @Inject constructor() : ViewModel() {
    private val _leftTime = MutableLiveData(SEC_MAX_LOCK_TIME)
    val leftTime: LiveData<Int> = _leftTime

    fun decreaseTime() {
        viewModelScope.launch {
            while ((leftTime.value ?: return@launch) > 0) {
                delay(1000L)
                _leftTime.value = leftTime.value?.minus(1)
            }
        }
    }

    companion object {
        private const val SEC_MAX_LOCK_TIME = 2400 // 2400000
    }
}
