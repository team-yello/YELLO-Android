package com.yello.presentation.onboarding.fragment.nameid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class NameidViewModel : ViewModel() {

    val _name = MutableLiveData("")
    val _id = MutableLiveData("")
    val _empty = MutableLiveData("")

    val isEmpty_name: LiveData<Boolean> =
        _name.map { name -> checkEmpty_name(name) }
    val isEmpty_id: LiveData<Boolean> =
        _id.map { id -> checkEmpty_id(id) }
    val isEmpty: LiveData<Boolean> =
        _empty.map { empty -> checkEmpty(id, name) }

    private val name: String
        get() = _name.value?.trim() ?: ""

    private val id: String
        get() = _id.value?.trim() ?: ""

    private val empty: String
        get() = _empty.value?.trim() ?: ""

    private fun checkEmpty_name(name: String): Boolean {
        return name.isEmpty()
    }

    private fun checkEmpty_id(id: String): Boolean {
        return id.isEmpty()
    }

    private fun checkEmpty(name: String, id: String): Boolean {
        return name.isEmpty() && id.isEmpty()
    }
}
