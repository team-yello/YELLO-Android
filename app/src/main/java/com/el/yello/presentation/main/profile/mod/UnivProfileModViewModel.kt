package com.el.yello.presentation.main.profile.mod

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.el.yello.presentation.main.profile.info.ProfileFragment.Companion.TYPE_UNIVERSITY
import com.example.domain.entity.ProfileModRequestModel
import com.example.domain.entity.onboarding.GroupList
import com.example.domain.entity.onboarding.SchoolList
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
class UnivProfileModViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _getUserDataResult = MutableSharedFlow<Boolean>()
    val getUserDataResult: SharedFlow<Boolean> = _getUserDataResult

    private val _getIsModValidResult = MutableSharedFlow<Boolean>()
    val getIsModValidResult: SharedFlow<Boolean> = _getIsModValidResult

    private val _postToModProfileResult = MutableSharedFlow<Boolean>()
    val postToModProfileResult: SharedFlow<Boolean> = _postToModProfileResult

    private val _getUnivListState = MutableStateFlow<UiState<SchoolList>>(UiState.Empty)
    val getUnivListState: StateFlow<UiState<SchoolList>> = _getUnivListState

    private val _getUnivGroupIdListState = MutableStateFlow<UiState<GroupList>>(UiState.Empty)
    val getUnivGroupIdListState: StateFlow<UiState<GroupList>> = _getUnivGroupIdListState

    val lastModDate = MutableLiveData("")
    val school = MutableLiveData("")
    val subGroup = MutableLiveData("")
    val admYear = MutableLiveData("")
    var groupId: Long = 0

    var isModAvailable = true
    var isChanged = false

    private lateinit var myUserData: ProfileModRequestModel

    val studentIdList = listOf(24, 23, 22, 21, 20, 19, 18, 17, 16, 15)

    private var currentPage = -1
    private var isPagingFinish = false
    private var totalPage = Int.MAX_VALUE

    fun setNewPage() {
        currentPage = -1
        isPagingFinish = false
        totalPage = Int.MAX_VALUE
    }

    fun resetStateVariables() {
        _getUnivListState.value = UiState.Empty
        _getUnivGroupIdListState.value = UiState.Empty
    }

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
                    myUserData = ProfileModRequestModel(
                        profile.name,
                        profile.yelloId,
                        profile.gender,
                        profile.email,
                        profile.profileImageUrl,
                        profile.groupId,
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

    fun getUnivListFromServer(searchText: String) {
        if (isPagingFinish) return
        viewModelScope.launch {
            onboardingRepository.getSchoolList(
                searchText,
                ++currentPage
            )
                .onSuccess { schoolList ->
                    if (schoolList == null) {
                        _getUnivListState.value = UiState.Empty
                        return@launch
                    }
                    totalPage = ceil((schoolList.totalCount * 0.1)).toInt() - 1
                    if (totalPage == currentPage) isPagingFinish = true
                    _getUnivListState.value = UiState.Success(schoolList)
                }
                .onFailure { t ->
                    _getUnivListState.value = UiState.Failure(t.message.toString())
                }
        }
    }

    fun getUnivGroupIdListFromServer(searchText: String) {
        viewModelScope.launch {
            _getUnivGroupIdListState.value = UiState.Loading
            onboardingRepository.getGroupList(
                ++currentPage,
                school.value ?: return@launch,
                searchText,
            )
                .onSuccess { groupList ->
                    if (groupList == null || groupList.totalCount == 0) {
                        _getUnivGroupIdListState.value = UiState.Empty
                        return@launch
                    }
                    totalPage = ceil((groupList.totalCount * 0.1)).toInt() - 1
                    if (totalPage == currentPage) isPagingFinish = true
                    _getUnivGroupIdListState.value = UiState.Success(groupList)
                }
                .onFailure { t ->
                    _getUnivGroupIdListState.value = UiState.Failure(t.message.toString())
                }
        }
    }

    companion object {
        const val TEXT_NONE = "-"
    }
}