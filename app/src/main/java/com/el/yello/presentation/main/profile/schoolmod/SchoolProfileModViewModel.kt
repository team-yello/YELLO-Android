package com.el.yello.presentation.main.profile.schoolmod

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.main.profile.info.ProfileFragment.Companion.TYPE_HIGH_SCHOOL
import com.el.yello.presentation.main.profile.info.ProfileFragment.Companion.TYPE_MIDDLE_SCHOOL
import com.el.yello.presentation.main.profile.univmod.UnivProfileModViewModel.Companion.TEXT_NONE
import com.example.domain.entity.ProfileModRequestModel
import com.example.domain.entity.onboarding.HighSchoolList
import com.example.domain.repository.OnboardingRepository
import com.example.domain.repository.ProfileRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class SchoolProfileModViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _getUserDataResult = MutableSharedFlow<Boolean>()
    val getUserDataResult: SharedFlow<Boolean> = _getUserDataResult

    private val _getIsModValidState = MutableStateFlow<UiState<Unit>>(UiState.Empty)
    val getIsModValidState: StateFlow<UiState<Unit>> = _getIsModValidState

    private val _getSchoolListState = MutableStateFlow<UiState<HighSchoolList>>(UiState.Empty)
    val getSchoolListState: StateFlow<UiState<HighSchoolList>> = _getSchoolListState

    private val _getSchoolGroupIdResult = MutableSharedFlow<Boolean>()
    val getSchoolGroupIdResult: SharedFlow<Boolean> = _getSchoolGroupIdResult

    private val _postToModProfileResult = MutableSharedFlow<Boolean>()
    val postToModProfileResult: SharedFlow<Boolean> = _postToModProfileResult

    val lastModDate = MutableLiveData("")
    val school = MutableLiveData("")
    val grade = MutableLiveData("")
    val classroom = MutableLiveData("")

    var isModAvailable = true
    var isChanged = false

    private lateinit var myUserData: ProfileModRequestModel

    val classroomList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20")

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    fun setNewPage() {
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
    }

    fun resetStateVariables() {
        _getSchoolListState.value = UiState.Empty
    }

    fun getUserDataFromServer() {
        viewModelScope.launch {
            profileRepository.getUserData()
                .onSuccess { profile ->
                    if (profile == null) {
                        _getUserDataResult.emit(false)
                        return@launch
                    }
                    if (profile.groupType == TYPE_HIGH_SCHOOL || profile.groupType == TYPE_MIDDLE_SCHOOL) {
                        school.value = profile.groupName
                        grade.value = profile.groupAdmissionYear.toString()
                        classroom.value = profile.subGroupName
                    } else {
                        school.value = TEXT_NONE
                        grade.value = TEXT_NONE
                        classroom.value = TEXT_NONE
                    }
                    myUserData = ProfileModRequestModel(
                        profile.name,
                        profile.yelloId,
                        profile.gender,
                        profile.email,
                        profile.profileImageUrl,
                        profile.groupId,
                        profile.groupAdmissionYear
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
            _getIsModValidState.value = UiState.Loading
            profileRepository.getModValidData()
                .onSuccess {
                    if (it == null) {
                        _getIsModValidState.value = UiState.Empty
                        return@launch
                    }
                    val splitValue = it.value.split("|")
                    isModAvailable = splitValue[0].toBoolean()
                    if (splitValue[1] != "null") {
                        lastModDate.value = splitValue[1].replace("-", ".")
                        _getIsModValidState.value = UiState.Success(Unit)
                    } else {
                        _getIsModValidState.value = UiState.Empty
                    }
                }
                .onFailure {
                    _getIsModValidState.value = UiState.Failure(it.message.orEmpty())
                }
        }
    }

    fun getSchoolListFromServer(searchText: String) {
        if (isPagingFinish) return
        viewModelScope.launch {
            onboardingRepository.getHighSchoolList(
                searchText,
                ++currentPage
            )
                .onSuccess { schoolList ->
                    if (schoolList == null) {
                        _getSchoolListState.value = UiState.Empty
                        return@launch
                    }
                    totalPage = ceil((schoolList.totalCount * 0.1)).toInt() - 1
                    if (totalPage == currentPage) isPagingFinish = true
                    _getSchoolListState.value = UiState.Success(schoolList)
                }
                .onFailure { t ->
                    _getSchoolListState.value = UiState.Failure(t.message.toString())
                }
        }
    }

    fun getSchoolGroupIdFromServer() {
        viewModelScope.launch {
            onboardingRepository.getGroupHighSchool(
                school.value ?: return@launch,
                classroom.value ?: return@launch,
            )
                .onSuccess { data ->
                    if (data == null) {
                        _getSchoolGroupIdResult.emit(false)
                        return@launch
                    }
                    myUserData.groupId = data.groupId
                    _getSchoolGroupIdResult.emit(true)
                    postNewProfileToServer()
                }
                .onFailure {
                    _getSchoolGroupIdResult.emit(false)
                }
        }
    }

    private fun postNewProfileToServer() {
        viewModelScope.launch {
            if (!::myUserData.isInitialized) {
                _postToModProfileResult.emit(false)
                return@launch
            }
            myUserData.groupAdmissionYear = grade.value?.toInt() ?: return@launch

            profileRepository.postToModUserData(myUserData)
                .onSuccess {
                    _postToModProfileResult.emit(true)
                }
                .onFailure {
                    _postToModProfileResult.emit(false)
                }
        }
    }
}