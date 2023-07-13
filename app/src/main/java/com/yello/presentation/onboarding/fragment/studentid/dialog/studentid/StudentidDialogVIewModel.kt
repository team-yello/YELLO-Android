package com.yello.presentation.onboarding.fragment.studentid.dialog.studentid


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.MyStudentid

class StudentidDialogVIewModel : ViewModel() {

    private val _studentidResult: MutableLiveData<List<MyStudentid>> = MutableLiveData()
    val studentidResult: LiveData<List<MyStudentid>> = _studentidResult

    fun addStudentId() {
        val mockList = listOf(
            MyStudentid("15학번"),
            MyStudentid("16학번"),
            MyStudentid("17학번"),
            MyStudentid("18학번"),
            MyStudentid("19학번"),
            MyStudentid("20학번"),
            MyStudentid("21학번"),
            MyStudentid("22학번"),
            MyStudentid("23학번"),

        )
        _studentidResult.value = mockList
    }
}
