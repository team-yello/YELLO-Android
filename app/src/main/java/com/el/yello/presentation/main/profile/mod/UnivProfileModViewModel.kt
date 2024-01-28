package com.el.yello.presentation.main.profile.mod

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.main.profile.info.ProfileFragment.Companion.TYPE_UNIVERSITY
import com.example.domain.entity.ProfileModRequestModel
import com.example.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnivProfileModViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _getUserDataResult = MutableSharedFlow<Boolean>()
    val getUserDataResult: SharedFlow<Boolean> = _getUserDataResult

    private val _getIsModValidResult = MutableSharedFlow<Boolean>()
    val getIsModValidResult: SharedFlow<Boolean> = _getIsModValidResult

    private val _postToModProfileResult = MutableSharedFlow<Boolean>()
    val postToModProfileResult: SharedFlow<Boolean> = _postToModProfileResult

    val lastModDate = MutableLiveData("")
    val school = MutableLiveData("")
    val subGroup = MutableLiveData("")
    val admYear = MutableLiveData("")

    var isModAvailable = true
    var isChanged = false

    private lateinit var myUserData: ProfileModRequestModel

    fun getUserDataFromServer() {
        viewModelScope.launch {
            profileRepository.getUserData()
                .onSuccess { profile ->
                    if (profile == null) {
                        _getUserDataResult.emit(false)
                        return@launch
                    }
                    if (profile.groupType == TYPE_UNIVERSITY) {
                        school.value = profile.groupName
                        subGroup.value = profile.subGroupName
                        admYear.value = profile.groupAdmissionYear.toString()
                    } else {
                        school.value = TEXT_NONE
                        subGroup.value = TEXT_NONE
                        admYear.value = TEXT_NONE
                    }
                    // TODO : groupId 수정
                    myUserData = ProfileModRequestModel(
                        profile.name,
                        profile.yelloId,
                        profile.gender,
                        profile.email,
                        profile.profileImageUrl,
                        0,
                        admYear.value?.toInt() ?: 0
                    )
                    _getUserDataResult.emit(true)
                }
                .onFailure {
                    _getUserDataResult.emit(false)
                }
        }
    }

    fun getIsModValidFromServer() {
        viewModelScope.launch {
            profileRepository.getModValidData()
                .onSuccess {
                    if (it == null) {
                        _getIsModValidResult.emit(false)
                        return@launch
                    }
                    val splitValue = it.value.split("|")
                    isModAvailable = splitValue[0].toBoolean()
                    lastModDate.value = splitValue[1].replace("-", ".")
                }
                .onFailure {
                    _getIsModValidResult.emit(false)
                }
        }
    }

    fun postNewProfileToServer() {
        viewModelScope.launch {
            if (!::myUserData.isInitialized) {
                _postToModProfileResult.emit(false)
                return@launch
            }
            profileRepository.postToModUserData(myUserData)
                .onSuccess {
                    _postToModProfileResult.emit(true)
                }
                .onFailure {
                    _postToModProfileResult.emit(false)
                }
        }
    }

    companion object {
        const val TEXT_NONE = "-"
    }

}