package com.yello.presentation.onboarding.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.MyName
import com.example.domain.entity.MyStudentid
import com.example.domain.entity.onboarding.Friend
import com.example.domain.entity.onboarding.FriendGroup
import com.example.domain.entity.onboarding.FriendList
import com.example.domain.entity.onboarding.MyCode
import com.example.domain.entity.onboarding.MyDepartment
import com.example.domain.entity.onboarding.MyGender
import com.example.domain.entity.onboarding.MyId
import com.example.domain.entity.onboarding.MySchool
import com.example.domain.repository.OnboardingRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {
    private val _schoolData = MutableLiveData<UiState<MySchool>>()
    val schoolData: MutableLiveData<UiState<MySchool>> = _schoolData

    private val _departmentData = MutableLiveData<UiState<MyDepartment>>()
    val departmentData: MutableLiveData<UiState<MyDepartment>> = _departmentData

    private val _friendData = MutableLiveData<UiState<FriendList>>()
    val friendData: MutableLiveData<UiState<FriendList>> = _friendData

    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

    var schoolPage = -1L
    private var isSchoolPagingFinish = false
    private var totalSchoolPage = Long.MAX_VALUE

    var departmentPage = -1L
    private var isDepartmentPagingFinish = false
    private var totalDepartmentPage = Long.MAX_VALUE

    var friendPage = -1L
    private var isFriendPagingFinish = false
    private var totalFriendPage = Long.MAX_VALUE

    val _school = MutableLiveData("")
    val _department = MutableLiveData("")
    val _studentId = MutableLiveData("")
    val _name = MutableLiveData("")
    val _friend = MutableLiveData("")
    val _id = MutableLiveData("")
    val _gender = MutableLiveData("")
    val _code = MutableLiveData("")

    fun addListSchool(search: String) {
        if (isSchoolPagingFinish) return
        viewModelScope.launch {
            _schoolData.value = UiState.Loading
            onboardingRepository.getSchoolService(
                search,
                ++schoolPage,
            ).onSuccess { school ->
                if (school == null) {
                    _schoolData.value = UiState.Empty
                    return@launch
                }
                totalSchoolPage = Math.ceil((school.totalCount * 0.1)).toLong()
                if (totalSchoolPage == schoolPage) isSchoolPagingFinish = true
                _schoolData.value =
                    when {
                        school.groupNameList.isEmpty() -> UiState.Empty
                        else -> UiState.Success(school)
                    }
            }.onFailure {
                _schoolData.value = UiState.Failure("대학교 불러오기 서버 통신 실패")
            }
        }
    }

    fun addListDepartment(school: String, search: String) {
        if (isDepartmentPagingFinish) return
        viewModelScope.launch {
            _departmentData.value = UiState.Loading
            onboardingRepository.getDepartmentService(
                school,
                search,
                ++departmentPage,
            ).onSuccess { department ->
                if (department == null) {
                    _departmentData.value = UiState.Empty
                    return@launch
                }
                totalDepartmentPage = Math.ceil((department.totalCount * 0.1)).toLong()
                if (totalDepartmentPage == departmentPage) isDepartmentPagingFinish = true
                _departmentData.value =
                    when {
                        department.groupList.isEmpty() -> UiState.Empty
                        else -> UiState.Success(department)
                    }
            }
        }
    }
    fun addListFriend(friendGroup: FriendGroup) {
        if (isFriendPagingFinish) return
        viewModelScope.launch {
            _friendData.value = UiState.Loading
            onboardingRepository.postFriendService(
                friendGroup,
                ++friendPage,
            ).onSuccess { friend ->
                if (friend == null) {
                    _friendData.value = UiState.Empty
                    return@launch
                }
                totalFriendPage = Math.ceil((friend.totalCount * 0.1)).toLong()
                if (totalFriendPage == friendPage) isFriendPagingFinish = true
                _friendData.value =
                    when {
                        friend.FriendList.isEmpty() -> UiState.Empty
                        else -> UiState.Success(friend)
                    }
            }
        }
    }

    fun setSchool(school: String) {
        _school.value = school
    }

    fun setDepartment(department: String) {
        _department.value = department
    }

    fun setStudentId(studentId: String) {
        _studentId.value = studentId
    }

    fun setFriend(friend: Friend) {
        _friend.value = friend.toString()
    }

    val isValidSchool: LiveData<Boolean> = _school.map { school -> checkValidSchool(school) }

    val isEmptyDepartment: LiveData<Boolean> =
        _department.map { department -> checkEmptyDepartment(department) }
    val isEmptyStudentId: LiveData<Boolean> =
        _studentId.map { studentId -> checkEmptyStudentId(studentId) }
    val isEmptyName: LiveData<Boolean> =
        _name.map { name -> checkEmptyName(name) }
    val isEmptyId: LiveData<Boolean> =
        _id.map { id -> checkEmptyId(id) }
    val isEmptyCode: LiveData<Boolean> =
        _code.map { code -> checkEmptyCode(code) }

    private val school: String
        get() = _school.value?.trim() ?: ""

    private val department: String
        get() = _department.value?.trim() ?: ""

    private val studentId: String
        get() = _studentId.value?.trim() ?: ""

    private val name: String
        get() = _name.value?.trim() ?: ""

    private val id: String
        get() = _id.value?.trim() ?: ""

    private val gender: String
        get() = _gender.value?.trim() ?: ""

    private val code: String
        get() = _code.value?.trim() ?: ""

    private val _schoolResult: MutableLiveData<List<MySchool>> = MutableLiveData()
    val schoolResult: LiveData<List<MySchool>> = _schoolResult

    private val _departmentResult: MutableLiveData<List<MyDepartment>> = MutableLiveData()
    val departmentResult: LiveData<List<MyDepartment>> = _departmentResult

    private val _studentIdResult: MutableLiveData<List<MyStudentid>> = MutableLiveData()
    val studentIdResult: LiveData<List<MyStudentid>> = _studentIdResult

    private val _friendResult: MutableLiveData<List<Friend>> = MutableLiveData()
    val friendResult: LiveData<List<Friend>> = _friendResult

    private val _idResult: MutableLiveData<List<MyId>> = MutableLiveData()
    val idResult: LiveData<List<MyId>> = _idResult

    private val _nameResult: MutableLiveData<List<MyName>> = MutableLiveData()
    val nameResult: LiveData<List<MyName>> = _nameResult

    private val _genderResult: MutableLiveData<List<MyGender>> = MutableLiveData()
    val genderResult: LiveData<List<MyGender>> = _genderResult

    private val _codeResult: MutableLiveData<List<MyCode>> = MutableLiveData()
    val codeResult: LiveData<List<MyCode>> = _codeResult

    fun checkValidSchool(school: String): Boolean {
        return school.isNullOrBlank()
    }

    fun checkEmptyDepartment(department: String): Boolean {
        return department.isNullOrBlank()
    }

    fun checkEmptyStudentId(studentid: String): Boolean {
        return studentid.isNullOrBlank()
    }

    fun checkEmptyName(name: String): Boolean {
        return name.isNullOrBlank()
    }
    private fun checkRegexName(name: String): Boolean {
        return name.matches("^[ㄱ-ㅎㅏ-ㅣ가-힣]\$".toRegex())
    }

    private fun checkRegexId(id: String): Boolean {
        return id.matches("^[A-Za-z0-9_.]*\$".toRegex())
    }

    fun checkEmptyId(id: String): Boolean {
        return id.isNullOrBlank()
    }
    fun checkEmptyCode(code: String): Boolean {
        return code.isNullOrBlank()
    }
    fun navigateToNextPage() {
        _currentPage.value = currentPage.value?.plus(1)
    }

    fun navigateToBackPage() {
        _currentPage.value = currentPage.value?.minus(1)
    }

    fun addStudentId() {
        val mockList = listOf(
            MyStudentid("15학번"),
            MyStudentid("16학번"),
            MyStudentid("17학번"),
            MyStudentid("18학번"),
            MyStudentid("19학번"),
            MyStudentid("20학번"),
            MyStudentid("21학번"),
            MyStudentid("22학번"),
            MyStudentid("23학번"),
        )
        _studentIdResult.value = mockList
    }
}
