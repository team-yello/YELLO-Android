package com.yello.presentation.onboarding.fragment.studentid.dialog.Department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.MyDepartment

class DepartmentViewModel : ViewModel() {

    private val _departmentResult: MutableLiveData<List<MyDepartment>> = MutableLiveData()
    val departmentResult: LiveData<List<MyDepartment>> = _departmentResult

    fun addDepartment() {
        val mockList = listOf(
            MyDepartment(""),
            MyDepartment("데학"),
            MyDepartment("ㅇ너란ㅇㄹ"),
            MyDepartment("ㅇ너란ㅇㄹ"),
            MyDepartment("ㅇ너란ㅇㄹ"),
            MyDepartment("ㅇ너란ㅇㄹ"),
        )
        _departmentResult.value = mockList
    }
}
