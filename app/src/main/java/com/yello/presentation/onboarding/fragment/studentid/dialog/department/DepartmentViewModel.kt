package com.yello.presentation.onboarding.fragment.studentid.dialog.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.MyDepartment

class DepartmentViewModel : ViewModel() {

    private val _departmentResult: MutableLiveData<List<MyDepartment>> = MutableLiveData()
    val departmentResult: LiveData<List<MyDepartment>> = _departmentResult

    fun addDepartment() {
        val mockList = listOf(
            MyDepartment("경영학과"),
            MyDepartment("컴퓨터 공학과"),
            MyDepartment("사랑해학과"),
            MyDepartment("옐로최고학과"),
            MyDepartment("개발자양성학과"),
            MyDepartment("법학과"),
        )
        _departmentResult.value = mockList
    }
}
