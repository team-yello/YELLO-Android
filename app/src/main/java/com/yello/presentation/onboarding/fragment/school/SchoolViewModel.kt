package com.yello.presentation.onboarding.fragment.school

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.MySchool

class SchoolViewModel : ViewModel() {

    private val _schoolResult: MutableLiveData<List<MySchool>> = MutableLiveData()
    val schoolResult: LiveData<List<MySchool>> = _schoolResult

    fun addSchool() {
        val mockList = listOf(
            MySchool("김상호랑이대학교"),
            MySchool("전채연습만이살길대학교"),
            MySchool("이강민머리될떄까지대학교"),
            MySchool("박민주거라연습대학교"),
        )
        _schoolResult.value = mockList
    }
}
