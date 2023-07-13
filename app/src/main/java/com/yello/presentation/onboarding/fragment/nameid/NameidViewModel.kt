package com.yello.presentation.onboarding.fragment.nameid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NameidViewModel : ViewModel() {

    val _name = MutableLiveData("")
    val _id = MutableLiveData("")

    private val name: String
        get() = _name.value?.trim() ?: ""

    private val id: String
        get() = _id.value?.trim() ?: ""
}
