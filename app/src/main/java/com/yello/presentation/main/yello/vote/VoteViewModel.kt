package com.yello.presentation.main.yello.vote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.vote.Choice
import com.example.domain.entity.vote.Note
import com.example.domain.repository.VoteRepository
import com.example.ui.view.UiState
import com.example.ui.view.UiState.Empty
import com.example.ui.view.UiState.Success
import com.yello.presentation.main.yello.vote.NoteState.InvalidCancel
import com.yello.presentation.main.yello.vote.NoteState.InvalidShuffle
import com.yello.presentation.main.yello.vote.NoteState.InvalidSkip
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val voteRepository: VoteRepository,
) : ViewModel() {
    private val _noteState = MutableLiveData<NoteState>()
    val noteState: LiveData<NoteState>
        get() = _noteState

    val _voteState = MutableLiveData<UiState<List<Note>>>()
    val voteState: LiveData<UiState<List<Note>>>
        get() = _voteState

    private val _shuffleCount = MutableLiveData(MAX_COUNT_SHUFFLE)
    val shuffleCount: LiveData<Int>
        get() = _shuffleCount

    private val _backgroundIndex = MutableLiveData<Int>()
    val backgroundIndex: Int
        get() = _backgroundIndex.value ?: 0

    val _currentNoteIndex = MutableLiveData<Int>()
    val currentNoteIndex: Int
        get() = _currentNoteIndex.value ?: 0

    val _currentChoice = MutableLiveData(Choice(-1))
    val currentChoice: Choice
        get() = requireNotNull(_currentChoice.value)

    val _choiceList = MutableLiveData(mutableListOf<Choice>())
    val choiceList: MutableList<Choice>
        get() = requireNotNull(_choiceList.value)

    val _voteList = MutableLiveData<List<Note>>()
    val voteList: List<Note>
        get() = requireNotNull(_voteList.value)

    init {
        initVoteIndex()
        getVoteQuestions()
    }

    private fun getVoteQuestions() {
        viewModelScope.launch {
            voteRepository.getVoteQuestion()
                .onSuccess { notes ->
                    Timber.d("GET VOTE QUESTION SUCCESS : $notes")
                    if (notes == null) {
                        _voteState.value = Empty
                        return@launch
                    }
                    _voteState.value = Success(notes)
                    _voteList.value = notes
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("GET VOTE QUESTION FAILURE : $t")
                        _voteState.value = UiState.Failure(t.code().toString())
                    } else {
                        Timber.e("GET VOTE QUESTION ERROR : $t")
                        _voteState.value = UiState.Failure(t.message.toString()) // TODO: 서버 수정 후 else 제거
                    }
                }
        }
    }

    fun selectName(nameIndex: Int) {
        if (currentNoteIndex > INDEX_FINAL_VOTE) return
        if (currentChoice.friendId == voteList[currentNoteIndex].friendList[nameIndex].id) {
            _noteState.value = InvalidCancel
            return
        }
        if (currentChoice.friendId != null) return
        with(voteList[currentNoteIndex].friendList[nameIndex]) {
            _currentChoice.value?.friendId = id
            _currentChoice.value?.friendName = name
            _currentChoice.value = _currentChoice.value
        }

        currentChoice.keywordName ?: return
        _choiceList.value?.add(currentChoice)
        viewModelScope.launch {
            delay(DELAY_OPTION_SELECTION)
            skipToNextVote()
        }
    }

    fun selectKeyword(keywordIndex: Int) {
        if (currentNoteIndex > INDEX_FINAL_VOTE) return
        if (currentChoice.keywordName == voteList[currentNoteIndex].keywordList[keywordIndex]) {
            _noteState.value = InvalidCancel
            return
        }
        if (currentChoice.keywordName != null) return
        _currentChoice.value?.keywordName = voteList[currentNoteIndex].keywordList[keywordIndex]
        _currentChoice.value = _currentChoice.value

        currentChoice.friendId ?: return
        _choiceList.value?.add(currentChoice)
        viewModelScope.launch {
            delay(DELAY_OPTION_SELECTION)
            skipToNextVote()
        }
    }

    fun shuffle() {
        shuffleCount.value?.let { count ->
            if (currentChoice.friendId != null) {
                _noteState.value = InvalidShuffle
                return
            }
            if (count < 1) return

            viewModelScope.launch {
                voteRepository.getFriendShuffle()
                    .onSuccess { friends ->
                        Timber.d("GET FRIEND SHUFFLE SUCCESS : $friends")
                        if (friends == null) {
                            _noteState.value = NoteState.Failure
                            return@launch
                        }
                        _shuffleCount.value = count - 1
                        _voteList.value?.get(currentNoteIndex)?.friendList = friends
                        _voteList.value = voteList
                    }
                    .onFailure { t ->
                        if (t is HttpException) {
                            Timber.e("GET FRIEND SHUFFLE FAILURE : $t")
                            _noteState.value = NoteState.Failure
                        }
                    }
            }
        }
    }

    fun skip() {
        if (isOptionSelected()) {
            _noteState.value = InvalidSkip
            return
        }
        skipToNextVote()
    }

    private fun initCurrentChoice() {
        // TODO: 명세서 수정 되면 받아서 questionId 넣어주기
        _currentChoice.value = Choice(1)
    }

    private fun initVoteIndex() {
        _backgroundIndex.value = (0..11).random()
        _currentNoteIndex.value = 0
        initCurrentChoice()
    }

    private fun skipToNextVote() {
        initCurrentChoice()
        _noteState.value = NoteState.Success
        _shuffleCount.value = MAX_COUNT_SHUFFLE
        _currentNoteIndex.value = currentNoteIndex + 1
    }

    private fun isOptionSelected() =
        currentChoice.friendId != null || currentChoice.keywordName != null

    companion object {
        private const val MAX_COUNT_SHUFFLE = 3
        private const val INDEX_FINAL_VOTE = 9
        private const val DELAY_OPTION_SELECTION = 1000L
    }
}
