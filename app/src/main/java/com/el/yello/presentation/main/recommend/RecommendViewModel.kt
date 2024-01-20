package com.el.yello.presentation.main.recommend.kakao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.main.recommend.list.RecommendViewHolder
import com.example.domain.entity.RecommendListModel
import com.example.domain.entity.RecommendRequestModel
import com.example.domain.entity.RecommendUserInfoModel
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.RecommendRepository
import com.example.ui.view.UiState
import com.kakao.sdk.talk.TalkApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _getKakaoErrorResult = MutableStateFlow(false)
    val getKakaoErrorResult: StateFlow<Boolean> = _getKakaoErrorResult

    private val _postFriendsListState = MutableStateFlow<UiState<RecommendListModel>>(UiState.Empty)
    val postFriendsListState: StateFlow<UiState<RecommendListModel>> = _postFriendsListState

    private val _addFriendState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val addFriendState: StateFlow<UiState<Unit>> = _addFriendState

    private val _getUserDataState = MutableStateFlow<UiState<RecommendUserInfoModel>>(UiState.Empty)
    val getUserDataState: StateFlow<UiState<RecommendUserInfoModel>> = _getUserDataState

    var isSearchViewShowed = false

    var itemPosition: Int? = null
    var itemHolder: RecommendViewHolder? = null
    var clickedUserData = RecommendUserInfoModel(0, "", "", "", "", 0, 0)

    private var currentOffset = -100
    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    private var isFirstFriendsListPage: Boolean = true

    fun setFirstPageLoading() {
        isFirstFriendsListPage = true
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
        _postFriendsListState.value = UiState.Empty
        _addFriendState.value = UiState.Empty
    }

    fun setPositionAndHolder(position: Int, holder: RecommendViewHolder) {
        itemPosition = position
        itemHolder = holder
    }

    fun initViewModelVariable() {
        currentOffset = -100
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
        _postFriendsListState.value = UiState.Empty
        _addFriendState.value = UiState.Empty
        _getKakaoErrorResult.value = false
    }

    // 서버 통신 - 카카오 리스트 통신 후 친구 리스트 추가 서버 통신 진행
    fun addListWithKakaoIdList() {
        if (isPagingFinish) return
        currentOffset += 100
        currentPage += 1
        if (isFirstFriendsListPage) {
            _postFriendsListState.value = UiState.Loading
            isFirstFriendsListPage = false
        }
        TalkApiClient.instance.friends(offset = currentOffset, limit = 100) { friends, error ->
            if (error != null) {
                _getKakaoErrorResult.value = true
            } else if (friends != null) {
                totalPage = ceil((friends.totalCount * 0.01)).toInt() - 1
                if (totalPage == currentPage) isPagingFinish = true
                getListFromServer(
                    friends.elements?.map { friend -> friend.id.toString() } ?: listOf(),
                )
            }
        }
    }

    // 서버 통신 - 학교 친구 리스트 추가
    fun addListGroupFromServer() {
        viewModelScope.launch {
            if (isPagingFinish) return@launch
            if (isFirstFriendsListPage) {
                _postFriendsListState.value = UiState.Loading
                isFirstFriendsListPage = false
            }
            recommendRepository.getSchoolFriendList(
                ++currentPage,
            )
                .onSuccess {
                    it ?: return@launch
                    totalPage = ceil((it.totalCount * 0.01)).toInt() - 1
                    if (totalPage == currentPage) isPagingFinish = true
                    _postFriendsListState.value = UiState.Success(it)
                }
                .onFailure {
                    _postFriendsListState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    // 서버 통신 - 추천 친구 리스트 추가
    private fun getListFromServer(friendsKakaoId: List<String>) {
        viewModelScope.launch {
            recommendRepository.postToGetKakaoFriendList(
                0,
                RecommendRequestModel(friendsKakaoId),
            )
                .onSuccess {
                    it ?: return@launch
                    _postFriendsListState.value = UiState.Success(it)
                }
                .onFailure {
                    _postFriendsListState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    // 서버 통신 - 친구 추가
    fun addFriendToServer(friendId: Long) {
        _addFriendState.value = UiState.Loading
        viewModelScope.launch {
            recommendRepository.postFriendAdd(friendId)
                .onSuccess {
                    _addFriendState.value = UiState.Success(it)
                }
                .onFailure {
                    _addFriendState.value = UiState.Failure(it.message.toString())
                }
        }
    }

    // 서버 통신 - 특정 유저 정보 받아 오기
    fun getUserDataFromServer(userId: Long) {
        viewModelScope.launch {
            _getUserDataState.value = UiState.Loading
            recommendRepository.getRecommendUserInfo(userId)
                .onSuccess { userInfo ->
                    if (userInfo == null) {
                        _getUserDataState.value = UiState.Empty
                        return@launch
                    }
                    _getUserDataState.value = UiState.Success(userInfo)
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        _getUserDataState.value = UiState.Failure(t.message.toString())
                    }
                }
        }
    }

    fun getYelloId() = authRepository.getYelloId()
}
