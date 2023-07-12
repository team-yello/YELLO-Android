package com.yello.presentation.onboarding.fragment.nameid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import java.util.regex.Pattern

class NameidViewModel : ViewModel() {

    val _name = MutableLiveData("")
    val _id = MutableLiveData("")
    val _empty = MutableLiveData("")
    val _name_error = MutableLiveData("")

    val isEmpty_name: LiveData<Boolean> =
        _name.map { name -> checkEmpty_name(name) }
    val isEmpty_id: LiveData<Boolean> =
        _id.map { id -> checkEmpty_id(id) }
    val isEmpty: LiveData<Boolean> =
        _empty.map { empty -> checkEmpty(id, name) }
    val is_name_error: LiveData<Boolean> =
        _name_error.map { empty -> checkNameError(name) }

    private val name: String
        get() = _name.value?.trim() ?: ""

    private val id: String
        get() = _id.value?.trim() ?: ""

    private val empty: String
        get() = _empty.value?.trim() ?: ""

    private val name_error: String
        get() = _name_error.value?.trim() ?: ""

    private fun checkEmpty_name(name: String): Boolean {
        return name.isEmpty()
    }

    private fun checkEmpty_id(id: String): Boolean {
        return id.isEmpty()
    }

    private fun checkEmpty(name: String, id: String): Boolean {
        return name.isEmpty() && id.isEmpty()
    }

    private fun checkNameError(name_error: String): Boolean {
        return Pattern.matches(REGEX_NAME_PATTERN, name_error)
    }

    companion object {
        private const val REGEX_NAME_PATTERN = "^[ㄱ-ㅎ가-힣]*$"
    }
}
