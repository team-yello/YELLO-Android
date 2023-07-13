package com.yello.presentation.main.yello.vote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Choice
import com.example.domain.entity.Note
import com.example.domain.entity.Note.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor() : ViewModel() {
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
        _voteList.value = mutableListOf(
            Note(
                nameHead = "나는",
                nameFoot = "이(가)",
                keywordHead = "",
                keywordFoot = "닮은 것 같아",
                friendList = listOf(
                    Friend(
                        id = 1,
                        yelloId = "kev_hy1042",
                        name = "김효원",
                    ),
                    Friend(
                        id = 2,
                        yelloId = "hj__p_",
                        name = "권세훈",
                    ),
                    Friend(
                        id = 3,
                        yelloId = "_euije",
                        name = "이강민",
                    ),
                    Friend(
                        id = 4,
                        yelloId = "nahyunyou",
                        name = "이의제",
                    ),
                ),
                keywordList = listOf(
                    "99대장 나선욱이랑",
                    "skrr하는 송민호랑",
                    "범죄도시 손석구랑",
                    "코딩하는 강동원이랑",
                ),
            ),
            Note(
                nameHead = "",
                nameFoot = "의 은밀한 취미는",
                keywordHead = "",
                keywordFoot = "일 것 같아",
                friendList = listOf(
                    Friend(
                        id = 5,
                        yelloId = "chae.yeon1004",
                        name = "전채연",
                    ),
                    Friend(
                        id = 6,
                        yelloId = "k.mean.e",
                        name = "이강민",
                    ),
                    Friend(
                        id = 7,
                        yelloId = "sangho.kk",
                        name = "김상호",
                    ),
                    Friend(
                        id = 8,
                        yelloId = "filminju_",
                        name = "박민주",
                    ),
                ),
                keywordList = listOf(
                    "애니 코스프레",
                    "발냄새 맡기",
                    "피규어 모으기",
                    "헌팅하기",
                ),
            ),
            Note(
                nameHead = "나는",
                nameFoot = "이(가)",
                keywordHead = "",
                keywordFoot = "닮은 것 같아",
                friendList = listOf(
                    Friend(
                        id = 1,
                        yelloId = "kev_hy1042",
                        name = "김효원",
                    ),
                    Friend(
                        id = 2,
                        yelloId = "hj__p_",
                        name = "권세훈",
                    ),
                    Friend(
                        id = 3,
                        yelloId = "_euije",
                        name = "이강민",
                    ),
                    Friend(
                        id = 4,
                        yelloId = "nahyunyou",
                        name = "이의제",
                    ),
                ),
                keywordList = listOf(
                    "99대장 나선욱이랑",
                    "skrr하는 송민호랑",
                    "범죄도시 손석구랑",
                    "코딩하는 강동원이랑",
                ),
            ),
            Note(
                nameHead = "",
                nameFoot = "의 은밀한 취미는",
                keywordHead = "",
                keywordFoot = "일 것 같아",
                friendList = listOf(
                    Friend(
                        id = 5,
                        yelloId = "chae.yeon1004",
                        name = "전채연",
                    ),
                    Friend(
                        id = 6,
                        yelloId = "k.mean.e",
                        name = "이강민",
                    ),
                    Friend(
                        id = 7,
                        yelloId = "sangho.kk",
                        name = "김상호",
                    ),
                    Friend(
                        id = 8,
                        yelloId = "filminju_",
                        name = "박민주",
                    ),
                ),
                keywordList = listOf(
                    "애니 코스프레",
                    "발냄새 맡기",
                    "피규어 모으기",
                    "헌팅하기",
                ),
            ),
            Note(
                nameHead = "나는",
                nameFoot = "이(가)",
                keywordHead = "",
                keywordFoot = "닮은 것 같아",
                friendList = listOf(
                    Friend(
                        id = 1,
                        yelloId = "kev_hy1042",
                        name = "김효원",
                    ),
                    Friend(
                        id = 2,
                        yelloId = "hj__p_",
                        name = "권세훈",
                    ),
                    Friend(
                        id = 3,
                        yelloId = "_euije",
                        name = "이강민",
                    ),
                    Friend(
                        id = 4,
                        yelloId = "nahyunyou",
                        name = "이의제",
                    ),
                ),
                keywordList = listOf(
                    "99대장 나선욱이랑",
                    "skrr하는 송민호랑",
                    "범죄도시 손석구랑",
                    "코딩하는 강동원이랑",
                ),
            ),
            Note(
                nameHead = "",
                nameFoot = "의 은밀한 취미는",
                keywordHead = "",
                keywordFoot = "일 것 같아",
                friendList = listOf(
                    Friend(
                        id = 5,
                        yelloId = "chae.yeon1004",
                        name = "전채연",
                    ),
                    Friend(
                        id = 6,
                        yelloId = "k.mean.e",
                        name = "이강민",
                    ),
                    Friend(
                        id = 7,
                        yelloId = "sangho.kk",
                        name = "김상호",
                    ),
                    Friend(
                        id = 8,
                        yelloId = "filminju_",
                        name = "박민주",
                    ),
                ),
                keywordList = listOf(
                    "애니 코스프레",
                    "발냄새 맡기",
                    "피규어 모으기",
                    "헌팅하기",
                ),
            ),
            Note(
                nameHead = "나는",
                nameFoot = "이(가)",
                keywordHead = "",
                keywordFoot = "닮은 것 같아",
                friendList = listOf(
                    Friend(
                        id = 1,
                        yelloId = "kev_hy1042",
                        name = "김효원",
                    ),
                    Friend(
                        id = 2,
                        yelloId = "hj__p_",
                        name = "권세훈",
                    ),
                    Friend(
                        id = 3,
                        yelloId = "_euije",
                        name = "이강민",
                    ),
                    Friend(
                        id = 4,
                        yelloId = "nahyunyou",
                        name = "이의제",
                    ),
                ),
                keywordList = listOf(
                    "99대장 나선욱이랑",
                    "skrr하는 송민호랑",
                    "범죄도시 손석구랑",
                    "코딩하는 강동원이랑",
                ),
            ),
            Note(
                nameHead = "",
                nameFoot = "의 은밀한 취미는",
                keywordHead = "",
                keywordFoot = "일 것 같아",
                friendList = listOf(
                    Friend(
                        id = 5,
                        yelloId = "chae.yeon1004",
                        name = "전채연",
                    ),
                    Friend(
                        id = 6,
                        yelloId = "k.mean.e",
                        name = "이강민",
                    ),
                    Friend(
                        id = 7,
                        yelloId = "sangho.kk",
                        name = "김상호",
                    ),
                    Friend(
                        id = 8,
                        yelloId = "filminju_",
                        name = "박민주",
                    ),
                ),
                keywordList = listOf(
                    "애니 코스프레",
                    "발냄새 맡기",
                    "피규어 모으기",
                    "헌팅하기",
                ),
            ),
            Note(
                nameHead = "나는",
                nameFoot = "이(가)",
                keywordHead = "",
                keywordFoot = "닮은 것 같아",
                friendList = listOf(
                    Friend(
                        id = 1,
                        yelloId = "kev_hy1042",
                        name = "김효원",
                    ),
                    Friend(
                        id = 2,
                        yelloId = "hj__p_",
                        name = "권세훈",
                    ),
                    Friend(
                        id = 3,
                        yelloId = "_euije",
                        name = "이강민",
                    ),
                    Friend(
                        id = 4,
                        yelloId = "nahyunyou",
                        name = "이의제",
                    ),
                ),
                keywordList = listOf(
                    "99대장 나선욱이랑",
                    "skrr하는 송민호랑",
                    "범죄도시 손석구랑",
                    "코딩하는 강동원이랑",
                ),
            ),
            Note(
                nameHead = "",
                nameFoot = "의 은밀한 취미는",
                keywordHead = "",
                keywordFoot = "일 것 같아",
                friendList = listOf(
                    Friend(
                        id = 5,
                        yelloId = "chae.yeon1004",
                        name = "전채연",
                    ),
                    Friend(
                        id = 6,
                        yelloId = "k.mean.e",
                        name = "이강민",
                    ),
                    Friend(
                        id = 7,
                        yelloId = "sangho.kk",
                        name = "김상호",
                    ),
                    Friend(
                        id = 8,
                        yelloId = "filminju_",
                        name = "박민주",
                    ),
                ),
                keywordList = listOf(
                    "애니 코스프레",
                    "발냄새 맡기",
                    "피규어 모으기",
                    "헌팅하기",
                ),
            ),
        )
    }

    fun selectName(nameIndex: Int) {
        if (currentNoteIndex > INDEX_FINAL_VOTE) return
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
            // TODO: 셔플 서버 통신 및 분기 처리
            if (isOptionSelected()) return
            if (count < 1) return
            _shuffleCount.value = count - 1
        }
    }

    fun skip() {
        if (isOptionSelected()) return
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
        _currentNoteIndex.value = currentNoteIndex + 1
        _shuffleCount.value = MAX_COUNT_SHUFFLE
        initCurrentChoice()
    }

    private fun isOptionSelected() = currentChoice.friendId != null || currentChoice.keywordName != null

    companion object {
        private const val MAX_COUNT_SHUFFLE = 3
        private const val INDEX_FINAL_VOTE = 9
        private const val DELAY_OPTION_SELECTION = 1000L
    }
}
