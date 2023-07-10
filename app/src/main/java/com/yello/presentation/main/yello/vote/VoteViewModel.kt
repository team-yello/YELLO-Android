package com.yello.presentation.main.yello.vote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor() : ViewModel() {
    private val _shuffleCount = MutableLiveData<Int>()
    val shuffleCount: LiveData<Int>
        get() = _shuffleCount

    private val _backgroundIndex = MutableLiveData<Int>()
    val backgroundIndex: Int
        get() = _backgroundIndex.value ?: 0

    val _currentNoteIndex = MutableLiveData<Int>()
    val currentNoteIndex: Int
        get() = _currentNoteIndex.value ?: 0

    init {
        initShuffleCount()
        initVoteIndex()
    }

    fun shuffle() {
        // TODO: 셔플 서버 통신 및 분기 처리
        shuffleCount.value?.let { count ->
            if (count < 1) return
            _shuffleCount.value = count - 1
        }
    }

    fun navigateToNextPage() {
        _currentNoteIndex.value = currentNoteIndex + 1
        initShuffleCount()
    }

    private fun initVoteIndex() {
        _backgroundIndex.value = (0..11).random()
        _currentNoteIndex.value = 0
    }

    private fun initShuffleCount() {
        _shuffleCount.value = MAX_SHUFFLE_COUNT
    }

    companion object {
        private const val MAX_SHUFFLE_COUNT = 3
    }
}
