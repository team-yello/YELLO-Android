package com.el.yello.presentation.main.profile.mod

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UnivProfileModViewModel @Inject constructor() : ViewModel() {

    val lastModDate = MutableLiveData("")
    val school = MutableLiveData("")
    val subGroup = MutableLiveData("")
    val admYear = MutableLiveData("")

}