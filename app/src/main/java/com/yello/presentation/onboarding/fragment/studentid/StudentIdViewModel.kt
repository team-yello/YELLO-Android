package com.yello.presentation.onboarding.fragment.studentid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class StudentIdViewModel : ViewModel() {

    val _department = MutableLiveData("")
    val _studentid = MutableLiveData("")
    val _empty = MutableLiveData("")

    val isEmpty_deaprtment: LiveData<Boolean> =
        _department.map { department -> checkEmpty_department(department) }
    val isEmpty_studentid: LiveData<Boolean> =
        _studentid.map { studentid -> checkEmpty_studentid(studentid) }
    val isEmpty: LiveData<Boolean> =
        _empty.map { empty -> checkEmpty(studentid, department) }

    private val department: String
        get() = _department.value?.trim() ?: ""

    private val studentid: String
        get() = _studentid.value?.trim() ?: ""

    private val empty: String
        get() = _empty.value?.trim() ?: ""

    private fun checkEmpty_department(department: String): Boolean {
        return department.isEmpty()
    }

    private fun checkEmpty_studentid(studentid: String): Boolean {
        return studentid.isEmpty()
    }

    private fun checkEmpty(studentid: String, department: String): Boolean {
        return studentid.isEmpty() && department.isEmpty()
    }
}
