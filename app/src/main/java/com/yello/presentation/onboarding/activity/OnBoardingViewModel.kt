package com.yello.presentation.onboarding.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.MyName
import com.example.domain.entity.MyStudentId
import com.example.domain.entity.onboarding.Friend
import com.example.domain.entity.onboarding.FriendGroup
import com.example.domain.entity.onboarding.FriendList
import com.example.domain.entity.onboarding.GroupList
import com.example.domain.entity.onboarding.MyCode
import com.example.domain.entity.onboarding.MyGender
import com.example.domain.entity.onboarding.MyId
import com.example.domain.entity.onboarding.SchoolList
import com.example.domain.repository.OnboardingRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {
    private val _schoolData = MutableLiveData<UiState<SchoolList>>()
    val schoolData: LiveData<UiState<SchoolList>> = _schoolData

    private val _departmentData = MutableLiveData<UiState<GroupList>>()
    val departmentData: LiveData<UiState<GroupList>> = _departmentData

    private val _friendData = MutableLiveData<UiState<FriendList>>()
    val friendData: MutableLiveData<UiState<FriendList>> = _friendData

    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

    var schoolPage = -1
    private var isSchoolPagingFinish = false
    private var totalSchoolPage = Integer.MAX_VALUE

    var departmentPage = -1
    private var isDepartmentPagingFinish = false
    private var totalDepartmentPage = Integer.MAX_VALUE

    var friendPage = -1L
    private var isFriendPagingFinish = false
    private var totalFriendPage = Long.MAX_VALUE

    val _school = MutableLiveData("")
    val school: String
        get() = _school.value?.trim() ?: ""

    private val _groupId = MutableLiveData<Long>()
    val groupId: LiveData<Long>
        get() = _groupId

    val _department = MutableLiveData("")
    private val department: String
        get() = _department.value?.trim() ?: ""

    val _studentId = MutableLiveData("")
    private val studentId: String
        get() = _studentId.value?.trim() ?: ""

    val _name = MutableLiveData("")
    private val name: String
        get() = _name.value?.trim() ?: ""

    val _friend = MutableLiveData("")
    val _id = MutableLiveData("")
    private val id: String
        get() = _id.value?.trim() ?: ""

    private val _gender = MutableLiveData("")
    val gender: String
        get() = _gender.value ?: ""

    private val _code = MutableLiveData("")
    val code: String
        get() = _code.value?.trim() ?: ""

    private val _profile = MutableLiveData("")
    val profile: String
        get() = _profile.value ?: ""

    // TODO: throttle 및 페이징 처리
    fun getSchoolList(search: String) {
        // if (isSchoolPagingFinish) return
        viewModelScope.launch {
            _schoolData.value = UiState.Loading
            onboardingRepository.getSchoolList(
                search,
                0,
                // ++schoolPage,
            ).onSuccess { schoolList ->
                Timber.d("GET SCHOOL LIST SUCCESS : $schoolList")
                if (schoolList == null) {
                    _schoolData.value = UiState.Empty
                    return@launch
                }
                // totalSchoolPage = ceil((schoolList.totalCount * 0.1)).toInt()
                // if (totalSchoolPage == schoolPage) isSchoolPagingFinish = true
                _schoolData.value =
                    when {
                        schoolList.schoolList.isEmpty() -> UiState.Empty
                        else -> UiState.Success(schoolList)
                    }
            }.onFailure { t ->
                if (t is HttpException) {
                    Timber.e("GET SCHOOL LIST FAILURE : $t")
                    _schoolData.value = UiState.Failure(t.code().toString())
                }
            }
        }
    }

    // TODO: throttle 및 페이징 처리
    fun getGroupList(search: String) {
        Timber.d("GET GROUP LIST 호출")
        // if (isDepartmentPagingFinish) return
        viewModelScope.launch {
            _departmentData.value = UiState.Loading
            onboardingRepository.getGroupList(
                school,
                search,
                0,
                // ++departmentPage,
            ).onSuccess { groupList ->
                Timber.d("GET GROUP LIST SUCCESS : $groupList")
                if (groupList == null) {
                    _departmentData.value = UiState.Empty
                    return@launch
                }

                // totalDepartmentPage = ceil((department.totalCount * 0.1)).toLong()
                // if (totalDepartmentPage == departmentPage) isDepartmentPagingFinish = true
                _departmentData.value =
                    when {
                        groupList.groupList.isEmpty() -> UiState.Empty
                        else -> UiState.Success(groupList)
                    }
            }.onFailure { t ->
                if (t is HttpException) {
                    Timber.e("GET GROUP LIST FAILURE : $t")
                    _departmentData.value = UiState.Failure(t.code().toString())
                }
                Timber.e("GET GROUP LIST ERROR : $t")
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
                totalFriendPage = kotlin.math.ceil((friend.totalCount * 0.1)).toLong()
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

    fun setGroupInfo(department: String, groupId: Long) {
        _department.value = department
        _groupId.value = groupId
    }

    fun setStudentId(studentId: String) {
        _studentId.value = studentId
    }

    fun setFriend(friend: Friend) {
        _friend.value = friend.toString()
    }

    fun clearSchoolData() {
        _schoolData.value = UiState.Success(SchoolList(0, emptyList()))
    }

    fun cleaDepartmentData() {
        _departmentData.value = UiState.Success(GroupList(0, emptyList()))
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

    private val _schoolResult: MutableLiveData<List<String>> = MutableLiveData()
    val schoolResult: LiveData<List<String>> = _schoolResult

    private val _departmentResult: MutableLiveData<List<GroupList>> = MutableLiveData()
    val departmentResult: LiveData<List<GroupList>> = _departmentResult

    private val _studentIdResult: MutableLiveData<List<MyStudentId>> = MutableLiveData()
    val studentIdResult: LiveData<List<MyStudentId>> = _studentIdResult

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

    private fun checkValidSchool(school: String): Boolean {
        return school.isNotBlank()
    }

    private fun checkEmptyDepartment(department: String): Boolean {
        return department.isBlank()
    }

    private fun checkEmptyStudentId(studentId: String): Boolean {
        return studentId.isBlank()
    }

    private fun checkEmptyName(name: String): Boolean {
        return name.isBlank()
    }

    private fun checkRegexName(name: String): Boolean {
        return name.matches("^[ㄱ-ㅎㅏ-ㅣ가-힣]\$".toRegex())
    }

    private fun checkRegexId(id: String): Boolean {
        return id.matches("^[A-Za-z0-9_.]*\$".toRegex())
    }

    private fun checkEmptyId(id: String): Boolean {
        return id.isBlank()
    }

    private fun checkEmptyCode(code: String): Boolean {
        return code.isBlank()
    }

    fun navigateToNextPage() {
        _currentPage.value = currentPage.value?.plus(1)
    }

    fun navigateToBackPage() {
        _currentPage.value = currentPage.value?.minus(1)
    }

    fun addStudentId() {
        val mockList = listOf(
            MyStudentId("15학번"),
            MyStudentId("16학번"),
            MyStudentId("17학번"),
            MyStudentId("18학번"),
            MyStudentId("19학번"),
            MyStudentId("20학번"),
            MyStudentId("21학번"),
            MyStudentId("22학번"),
            MyStudentId("23학번"),
        )
        _studentIdResult.value = mockList
    }
}
