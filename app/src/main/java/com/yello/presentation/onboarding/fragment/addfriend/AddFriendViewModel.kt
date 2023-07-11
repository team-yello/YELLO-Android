package com.yello.presentation.onboarding.fragment.addfriend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.MyFriend

class AddFriendViewModel : ViewModel() {

    private val _friendResult: MutableLiveData<List<MyFriend>> = MutableLiveData()
    val friendResult: LiveData<List<MyFriend>> = _friendResult

    fun addFriend() {
        val mockList = listOf(
            MyFriend(1, "김나현", "서울여자대학교 시각디자인과"),
            MyFriend(2, "강국희", "성신여자대학교 산업디자인과"),
            MyFriend(3, "이의제", "송민호대학교 컴퓨터공화과"),
            MyFriend(4, "고경표", "상호대학교 컴퓨터공학과"),
        )
        _friendResult.value = mockList
    }
}
