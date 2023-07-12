package com.yello.presentation.onboarding.fragment.school

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class SchoolViewModel : ViewModel() {

    val _school = MutableLiveData("")
    val isEmpty: LiveData<Boolean> = _school.map { school -> checkEmpty(school) }
    private val school: String
        get() = _school.value?.trim() ?: ""

    private fun checkEmpty(school: String): Boolean {
        return school.isEmpty()
    }
}
