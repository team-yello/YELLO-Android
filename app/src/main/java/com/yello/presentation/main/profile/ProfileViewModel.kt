package com.yello.presentation.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.ProfileFriendModel

class ProfileViewModel : ViewModel() {

    private val _friendsResult: MutableLiveData<List<ProfileFriendModel>> = MutableLiveData()
    val friendsResult: LiveData<List<ProfileFriendModel>> = _friendsResult

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

    fun addListFromLocal() {
        val mockList = listOf(
            ProfileFriendModel(1, "김상호", "@sangho.kk","기획대 손씻기과 19학번", null),
            ProfileFriendModel(2, "이강민", "@sangho.kim","디자인대 파트업무과 23학번", null),
            ProfileFriendModel(3, "전채연", "@kksangho","아요대 당번일과 25학번", null),
            ProfileFriendModel(4, "박민주", "@kimsangho","안드로이드안드로이드대 국희의콩나물국과 29학번", null),
            ProfileFriendModel(5, "공상호", "@sanghogogogogogog","기획대 손씻기과 19학번", null),
            ProfileFriendModel(6, "일강민", "@ananananandroid","디자인대 파트업무과 23학번", null),
            ProfileFriendModel(7, "이채연", "@yello_world","아요대 당번일과 25학번", null),
            ProfileFriendModel(8, "삼민주", "@sa_ng_ho__qdw","안드로이드안드로이드대 국희의콩나물국과 29학번", null),
            ProfileFriendModel(9, "사상호", "@sanghogogogogogog","기획대 손씻기과 19학번", null),
            ProfileFriendModel(10, "오강민", "@ananananandroid","디자인대 파트업무과 23학번", null),
            ProfileFriendModel(11, "육채연", "@yello_world","아요대 당번일과 25학번", null),
            ProfileFriendModel(12, "칠민주", "@sa_ng_ho__qdw","안드로이드안드로이드대 국희의콩나물국과 29학번", null)

        )
        _friendsResult.value = mockList
    }
}