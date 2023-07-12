package com.yello.presentation.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.ProfileFriendModel

class ProfileViewModel : ViewModel() {

    private val _friendsResult: MutableLiveData<List<ProfileFriendModel>> = MutableLiveData()
    val friendsResult: LiveData<List<ProfileFriendModel>> = _friendsResult

    fun addListFromLocal() {
        val mockList = listOf(
            ProfileFriendModel(1, "김상호", "기획대 손씻기과 19학번", null),
            ProfileFriendModel(2, "이강민", "디자인대 파트업무과 23학번", null),
            ProfileFriendModel(3, "전채연", "아요대 당번일과 25학번", null),
            ProfileFriendModel(4, "박민주", "안드로이드안드로이드대 국희의콩나물국과 29학번", null),
            ProfileFriendModel(5, "공상호", "기획대 손씻기과 19학번", null),
            ProfileFriendModel(6, "일강민", "디자인대 파트업무과 23학번", null),
            ProfileFriendModel(7, "이채연", "아요대 당번일과 25학번", null),
            ProfileFriendModel(8, "삼민주", "안드로이드안드로이드대 국희의콩나물국과 29학번", null)

        )
        _friendsResult.value = mockList
    }
}