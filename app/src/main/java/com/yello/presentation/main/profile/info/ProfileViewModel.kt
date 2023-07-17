package com.yello.presentation.main.profile.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.YelloDataStore
import com.example.domain.entity.ProfileFriendModel
import com.example.domain.entity.ProfileUserModel
import com.example.domain.repository.ProfileRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val dataStore: YelloDataStore
) : ViewModel() {

    private val _friendsResult: MutableLiveData<List<ProfileFriendModel>> = MutableLiveData()
    val friendsResult: LiveData<List<ProfileFriendModel>> = _friendsResult

    private val _getState = MutableLiveData<UiState<ProfileUserModel>>()
    val getState: LiveData<UiState<ProfileUserModel>> = _getState

    val myName: MutableLiveData<String> = MutableLiveData("")
    val myId: MutableLiveData<String> = MutableLiveData("")
    val mySchool: MutableLiveData<String> = MutableLiveData("")
    val myThumbnail: MutableLiveData<String> = MutableLiveData("")
    val myTotalMsg: MutableLiveData<String> = MutableLiveData("")
    val myTotalFriends: MutableLiveData<String> = MutableLiveData("")
    val myTotalPoints: MutableLiveData<String> = MutableLiveData("")

    val clickedItemName: MutableLiveData<String> = MutableLiveData("")
    val clickedItemId: MutableLiveData<String> = MutableLiveData("")
    val clickedItemSchool: MutableLiveData<String> = MutableLiveData("")
    val clickedItemThumbnail: MutableLiveData<String> = MutableLiveData("")
    val clickedItemTotalMsg: MutableLiveData<String> = MutableLiveData("")
    val clickedItemTotalFriends: MutableLiveData<String> = MutableLiveData("")

    fun getUserDataFromServer(userId: Int) {
        viewModelScope.launch {
            _getState.value = UiState.Loading
            runCatching {
                profileRepository.getUserData(userId)
            }.onSuccess {
                _getState.value = UiState.Success(it)
            }.onFailure {
                _getState.value = UiState.Failure("유저 정보 서버 통신 실패")
            }
        }
    }

    fun addListFromLocal() {
        val mockList = listOf(
            ProfileFriendModel(1, "김상호", "@sangho.kk", "기획대 손씻기과 19학번", null, 30, 27),
            ProfileFriendModel(2, "이강민", "@sangho.kim", "디자인대 파트업무과 23학번", null, 30, 27),
            ProfileFriendModel(3, "전채연", "@kksangho", "아요대 당번일과 25학번", null, 30, 27),
            ProfileFriendModel(4, "박민주", "@kimsangho", "안드로이드안드로이드대 국희의콩나물국과 29학번", null, 30, 27),
            ProfileFriendModel(5, "공상호", "@sanghogogogogogog", "기획대 손씻기과 19학번", null, 30, 27),
            ProfileFriendModel(6, "일강민", "@ananananandroid", "디자인대 파트업무과 23학번", null, 30, 27),
            ProfileFriendModel(7, "이채연", "@yello_world", "아요대 당번일과 25학번", null, 30, 27),
            ProfileFriendModel(
                8,
                "삼민주",
                "@sa_ng_ho__qdw",
                "안드로이드안드로이드대 국희의콩나물국과 29학번",
                null,
                30,
                27
            ),
            ProfileFriendModel(9, "사상호", "@sanghogogogogogog", "기획대 손씻기과 19학번", null, 30, 27),
            ProfileFriendModel(10, "오강민", "@ananananandroid", "디자인대 파트업무과 23학번", null, 30, 27),
            ProfileFriendModel(11, "육채연", "@yello_world", "아요대 당번일과 25학번", null, 30, 27),
            ProfileFriendModel(
                12,
                "칠민주",
                "@sa_ng_ho__qdw",
                "안드로이드안드로이드대 국희의콩나물국과 29학번",
                null,
                30,
                27
            )

        )
        _friendsResult.value = mockList
    }
}