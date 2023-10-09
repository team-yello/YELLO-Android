package com.el.yello.presentation.main.yello.vote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.main.yello.vote.NoteState.InvalidCancel
import com.el.yello.presentation.main.yello.vote.NoteState.InvalidShuffle
import com.el.yello.presentation.main.yello.vote.NoteState.InvalidSkip
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.entity.vote.Choice
import com.example.domain.entity.vote.ChoiceList
import com.example.domain.entity.vote.Note
import com.example.domain.entity.vote.StoredVote
import com.example.domain.repository.VoteRepository
import com.example.ui.view.UiState
import com.example.ui.view.UiState.Empty
import com.example.ui.view.UiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val voteRepository: VoteRepository,
) : ViewModel() {
    private val _noteState = MutableSharedFlow<NoteState>()
    val noteState: SharedFlow<NoteState>
        get() = _noteState.asSharedFlow()

    private val _voteState = MutableStateFlow<UiState<List<Note>>>(UiState.Loading)
    val voteState: StateFlow<UiState<List<Note>>>
        get() = _voteState.asStateFlow()

    private val _postVoteState = MutableStateFlow<UiState<Int>>(UiState.Loading)
    val postVoteState: StateFlow<UiState<Int>>
        get() = _postVoteState.asStateFlow()

    private val _shuffleCount = MutableStateFlow(MAX_COUNT_SHUFFLE)
    val shuffleCount: StateFlow<Int>
        get() = _shuffleCount.asStateFlow()

    private val _backgroundIndex = MutableStateFlow(0)
    val backgroundIndex: Int
        get() = _backgroundIndex.value

    val _currentNoteIndex = MutableStateFlow(0)
    val currentNoteIndex: Int
        get() = _currentNoteIndex.value

    val _currentChoice = MutableLiveData<Choice>()
    val currentChoice: Choice
        get() = requireNotNull(_currentChoice.value)

    val _choiceList = MutableStateFlow(mutableListOf<Choice>())
    val choiceList: MutableList<Choice>
        get() = requireNotNull(_choiceList.value)

    val _voteList = MutableStateFlow<List<Note>?>(null)
    val voteList: List<Note>
        get() = requireNotNull(_voteList.value)

    private val _votePointSum = MutableStateFlow(0)
    val votePointSum: Int
        get() = _votePointSum.value

    private val _totalPoint = MutableStateFlow(0)
    val totalPoint: Int
        get() = _totalPoint.value

    private var isTransitioning = false
    var totalListCount = Int.MAX_VALUE
        private set

    init {
        getStoredVote()
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
                    totalListCount = notes.size - 1
                    _voteState.value = Success(notes)
                    _voteList.value = notes
                    initVoteIndex()
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("GET VOTE QUESTION FAILURE : $t")
                        _voteState.value = UiState.Failure(t.code().toString())
                    } else {
                        Timber.e("GET VOTE QUESTION ERROR : $t")
                        _voteState.value =
                            UiState.Failure(t.message.toString()) // TODO: 서버 수정 후 else 제거
                    }
                }
        }
    }

    fun selectName(nameIndex: Int) {
        viewModelScope.launch {
            if (currentNoteIndex > totalListCount) return@launch
            if (currentChoice.friendId == voteList[currentNoteIndex].friendList[nameIndex].id) {
                _noteState.emit(InvalidCancel)
                return@launch
            }
            if (currentChoice.friendId != null) return@launch
            with(voteList[currentNoteIndex].friendList[nameIndex]) {
                _currentChoice.value?.friendId = id
                _currentChoice.value?.friendName = name
                _currentChoice.value = _currentChoice.value
            }

            currentChoice.keywordName ?: return@launch
            _choiceList.value.add(currentChoice)
            _votePointSum.value = votePointSum + voteList[currentNoteIndex].point
            viewModelScope.launch {
                delay(DELAY_OPTION_SELECTION)
                skipToNextVote()
            }
        }
    }

    fun selectKeyword(keywordIndex: Int) {
        viewModelScope.launch {
            if (currentNoteIndex > totalListCount) return@launch
            if (currentChoice.keywordName == voteList[currentNoteIndex].keywordList[keywordIndex]) {
                _noteState.emit(InvalidCancel)
                return@launch
            }
            if (currentChoice.keywordName != null) return@launch
            _currentChoice.value?.keywordName = voteList[currentNoteIndex].keywordList[keywordIndex]
            _currentChoice.value = _currentChoice.value

            currentChoice.friendId ?: return@launch
            _choiceList.value.add(currentChoice)
            _votePointSum.value = votePointSum + voteList[currentNoteIndex].point
            viewModelScope.launch {
                delay(DELAY_OPTION_SELECTION)
                skipToNextVote()
            }
        }
    }

    fun shuffle() {
        viewModelScope.launch {
            shuffleCount.value.let { count ->
                if (currentChoice.friendId != null) {
                    _noteState.emit(InvalidShuffle)
                    return@launch
                }
                if (count < 1) return@launch

                voteRepository.getFriendShuffle()
                    .onSuccess { friends ->
                        Timber.d("GET FRIEND SHUFFLE SUCCESS : $friends")
                        if (friends == null) {
                            _noteState.emit(NoteState.Failure)
                            return@launch
                        }
                        _shuffleCount.value = count - 1
                        _voteList.value?.get(currentNoteIndex)?.friendList = friends
                        _voteList.value = voteList
                    }
                    .onFailure { t ->
                        if (t is HttpException) {
                            Timber.e("GET FRIEND SHUFFLE FAILURE : $t")
                            _noteState.emit(NoteState.Failure)
                            return@launch
                        }
                    }
            }
        }
    }

    fun skip() {
        viewModelScope.launch {
            if (isOptionSelected()) {
                _noteState.emit(InvalidSkip)
                return@launch
            }

            if (isTransitioning) return@launch

            skipToNextVote()
        }
    }

    private fun postVote() {
        viewModelScope.launch {
            voteRepository.postVote(ChoiceList(choiceList = choiceList, totalPoint = votePointSum))
                .onSuccess { point ->
                    Timber.d("POST VOTE SUCCESS : $point")
                    if (point == null) {
                        _postVoteState.value = Empty
                        return@launch
                    }
                    voteRepository.clearStoredVote()
                    _postVoteState.value = Success(point)
                    _totalPoint.value = point
                    _currentNoteIndex.value = currentNoteIndex + 1
                    AmplitudeUtils.trackEventWithProperties("click_vote_finish")
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("POST VOTE FAILURE : $t")
                        _noteState.emit(NoteState.Failure)
                    }
                }
        }
    }

    private fun initCurrentChoice() {
        _currentChoice.value = Choice(
            questionId = voteList[currentNoteIndex].questionId,
            backgroundIndex = (backgroundIndex + currentNoteIndex) % 12 + 1,
        )
    }

    private fun initVoteIndex() {
        _backgroundIndex.value = (0..11).random()
        _currentNoteIndex.value = 0
        _votePointSum.value = 0
        initCurrentChoice()
    }

    private fun skipToNextVote() {
        viewModelScope.launch {
            if (currentNoteIndex == totalListCount) {
                postVote()
                return@launch
            }
            isTransitioning = true
            _noteState.emit(NoteState.Success)
            _shuffleCount.value = MAX_COUNT_SHUFFLE
            _currentNoteIndex.value = currentNoteIndex + 1
            voteRepository.setStoredVote(
                StoredVote(
                    currentIndex = currentNoteIndex,
                    choiceList = choiceList,
                    totalPoint = totalPoint,
                    questionList = voteList,
                ),
            )
            initCurrentChoice()
            viewModelScope.launch {
                delay(500L)
                isTransitioning = false
            }
        }
    }

    private fun getStoredVote() {
        val vote = voteRepository.getStoredVote()
        if (vote == null) {
            getVoteQuestions()
            return
        }
        totalListCount = vote.questionList.size - 1
        _voteList.value = vote.questionList
        _voteState.value = Success(vote.questionList)
        _choiceList.value = vote.choiceList
        _totalPoint.value = vote.totalPoint
        initCurrentChoice()
        viewModelScope.launch {
            delay(100L)
            _currentNoteIndex.value = vote.currentIndex
        }
    }

    private fun isOptionSelected() =
        currentChoice.friendId != null || currentChoice.keywordName != null

    companion object {
        private const val MAX_COUNT_SHUFFLE = 3
        private const val DELAY_OPTION_SELECTION = 1000L
    }
}
