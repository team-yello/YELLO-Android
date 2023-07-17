package com.yello.presentation.onboarding.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.MyCode
import com.example.domain.entity.MyDepartment
import com.example.domain.entity.MyFriend
import com.example.domain.entity.MyGender
import com.example.domain.entity.MyId
import com.example.domain.entity.MyName
import com.example.domain.entity.MySchool
import com.example.domain.entity.MyStudentid
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

    var schoolPage = -1L
    private var isSchoolPagingFinish = false
    private var totalSchoolPage = Long.MAX_VALUE

    fun initAddListSchool(search: String) {
        schoolPage = -1L
        addListSchool(search)
    }

    fun addListSchool(search: String) {
        if (isSchoolPagingFinish) return
        viewModelScope.launch {
            _schoolData.value = UiState.Loading
            onboardingRepository.getSchoolService(
                search,
                ++schoolPage,
            ).onSuccess {
                totalSchoolPage = Math.ceil((it.totalCount * 0.1)).toLong()
                if (totalSchoolPage == schoolPage) isSchoolPagingFinish = true
                _schoolData.value =
                    when {
                        it.groupNameList.isEmpty() -> UiState.Empty
                        else -> UiState.Success(it)
                    }
            }.onFailure {
                _schoolData.value = UiState.Failure("대학교 불러오기 서버 통신 실패")
            }
        }
    }

    val _school = MutableLiveData("")
    val _department = MutableLiveData("")
    val _studentid = MutableLiveData("")
    val _name = MutableLiveData("")
    val _id = MutableLiveData("")
    val _gender = MutableLiveData("")
    val _code = MutableLiveData("")
    val _profile = MutableLiveData("")

    fun setSchool(school: String) {
        _school.value = school
    }

    fun setDepartment(department: String) {
        _department.value = department
    }

    fun setStudentid(studentid: String) {
        _studentid.value = studentid
    }

    val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

    val isValidSchool: LiveData<Boolean> = _school.map { school -> checkValidSchool(school) }

    val isEmptyDepartment: LiveData<Boolean> =
        _department.map { department -> checkEmpty_department(department) }
    val isEmptyStudentid: LiveData<Boolean> =
        _studentid.map { studentid -> checkEmpty_studentid(studentid) }
    val isEmptyName: LiveData<Boolean> =
        _name.map { name -> checkEmpty_name(name) }
    val isEmptyId: LiveData<Boolean> =
        _id.map { id -> checkEmpty_id(id) }
    val isEmptyCode: LiveData<Boolean> =
        _code.map { code -> checkEmpty_code(code) }

    private val school: String
        get() = _school.value?.trim() ?: ""
    private val department: String
        get() = _department.value?.trim() ?: ""

    private val studentid: String
        get() = _studentid.value?.trim() ?: ""

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

    private val _studentidResult: MutableLiveData<List<MyStudentid>> = MutableLiveData()
    val studentidResult: LiveData<List<MyStudentid>> = _studentidResult

    private val _friendResult: MutableLiveData<List<MyFriend>> = MutableLiveData()
    val friendResult: LiveData<List<MyFriend>> = _friendResult

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

    fun checkEmpty_department(department: String): Boolean {
        return department.isNullOrBlank()
    }

    fun checkEmpty_studentid(studentid: String): Boolean {
        return studentid.isNullOrBlank()
    }

    fun checkEmpty_name(name: String): Boolean {
        return name.isNullOrBlank()
    }

    fun checkRegax_name(name: String): Boolean {
        return name.matches("^[ㄱ-ㅎㅏ-ㅣ가-힣]\$".toRegex())
    }

    // 문자, 숫자, 및줄, 마침표만 사용 정규표현식
    fun checkRegax_id(id: String): Boolean {
        return id.matches("^[A-Za-z0-9_.]*\$".toRegex())
    }

    fun checkEmpty_id(id: String): Boolean {
        return id.isNullOrBlank()
    }

    fun checkEmpty_code(code: String): Boolean {
        return code.isNullOrBlank()
    }

    // 목데이터
//    fun addSchool() {
//        val mockList = listOf(
//            MySchool(1, "김상호랑이대학교"),
//            MySchool(2, "전채연습만이살길대학교"),
//            MySchool(3, "이강민머리될떄까지대학교"),
//            MySchool(4, "박민주거라연습대학교"),
//        )
//        _schoolResult.value = mockList
//    }

    fun navigateToNextPage() {
        _currentPage.value = currentPage.value?.plus(1)
    }

    fun navigateToBackPage() {
        _currentPage.value = currentPage.value?.minus(1)
    }

//    fun addDepartment() {
//        val mockList = listOf(
//            MyDepartment(1, GroupList(1L, listOf<String>("경영학과", "컴퓨터 공학과")),
//            MyDepartment("컴퓨터 공학과"),
//            MyDepartment("사랑해학과"),
//            MyDepartment("옐로최고학과"),
//            MyDepartment("개발자양성학과"),
//            MyDepartment("법학과"),
//        )
//        _departmentResult.value = mockList
//    }

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
        _studentidResult.value = mockList
    }

    fun addFriend() {
        val mockList = listOf(
            MyFriend(1, "김나현", "서울여자대학교 시각디자인과"),
            MyFriend(2, "강국희", "성신여자대학교 산업디자인과"),
            MyFriend(3, "이의제", "송민호대학교 컴퓨터공화과"),
            MyFriend(4, "고경표", "상호대학교 컴퓨터공학과"),
        )
        _friendResult.value = mockList
    }
}
