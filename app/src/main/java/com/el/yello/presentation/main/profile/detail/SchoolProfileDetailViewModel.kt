package com.el.yello.presentation.main.profile.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SchoolProfileDetailViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    val name = MutableLiveData("")
    val id = MutableLiveData("")
    val school = MutableLiveData("")
    val grade = MutableLiveData("")
    val classroom = MutableLiveData("")

}