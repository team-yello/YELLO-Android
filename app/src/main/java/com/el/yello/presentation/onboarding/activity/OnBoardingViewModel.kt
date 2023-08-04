package com.el.yello.presentation.onboarding.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RequestOnboardingListModel
import com.example.domain.entity.onboarding.FriendGroup
import com.example.domain.entity.onboarding.FriendList
import com.example.domain.entity.onboarding.GroupList
import com.example.domain.entity.onboarding.SchoolList
import com.example.domain.entity.onboarding.SignupInfo
import com.example.domain.entity.onboarding.UserInfo
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.OnboardingRepository
import com.example.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val _studenttype = MutableLiveData("")
    val studenttype: String
        get() = _studenttype.value ?: ""

    fun selectStudenttype(studenttype: String) {
        _studenttype.value = studenttype
    }

    // 학교 viewmodel (step 1)

    private val _schoolData = MutableLiveData<UiState<SchoolList>>()
    val schoolData: MutableLiveData<UiState<SchoolList>> = _schoolData
    val _school = MutableLiveData("")
    val school: String
        get() = _school.value?.trim() ?: ""

    // 고등학생
    val _highschool = MutableLiveData("")
    val highschool: String
        get() = _highschool.value?.trim() ?: ""

    private val _inputText = MutableLiveData<String>()
    val inputText: LiveData<String>
        get() = _inputText

    fun setInputText(text: String) {
        _inputText.value = text
    }

    fun setSchool(school: String) {
        _school.value = school
    }

    fun clearSchoolData() {
        _schoolData.value = UiState.Success(SchoolList(0, emptyList()))
    }

    // TODO: Debounce 및 Paging3
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

    // 학과 학번 viewmodel (step 2)

    val _department = MutableLiveData("")
    private val department: String get() = _department.value?.trim() ?: ""

    private val _departmentData = MutableLiveData<UiState<GroupList>>()
    val departmentData: MutableLiveData<UiState<GroupList>> = _departmentData

    private val _groupId = MutableLiveData<Long>()
    val _studentId = MutableLiveData<Int>()

    private val _studentIdResult: MutableLiveData<List<Int>> = MutableLiveData()
    val studentIdResult: LiveData<List<Int>> = _studentIdResult
    val groupId: Long
        get() = requireNotNull(_groupId.value)
    private val studentId: Int
        get() = requireNotNull(_studentId.value)

    // 고등학교 학년
    val _grade = MutableLiveData("")
    val grade: String
        get() = _grade.value ?: ""

    fun selectGrade(grade: String?) {
        _grade.value = grade ?: ""
    }


    fun setGroupInfo(department: String, groupId: Long) {
        _department.value = department
        _groupId.value = groupId
    }

    fun setStudentId(studentId: Int) {
        _studentId.value = studentId
    }

    fun addStudentId() {
        val studentIdList = listOf(15, 16, 17, 18, 19, 20, 21, 22, 23)
        _studentIdResult.value = studentIdList
    }

    fun clearDepartmentData() {
        _departmentData.value = UiState.Success(GroupList(0, emptyList()))
    }

    // TODO: Debounce 및 Paging3
    fun getGroupList(search: String) {
        // if (isDepartmentPagingFinish) return
        viewModelScope.launch {
            _departmentData.value = UiState.Loading
            onboardingRepository.getGroupList(
                school,
                search,
                0,
                // ++departmentPage,
            ).onSuccess { groupList ->
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
    // 이름 아이디 viewmodel (step 3)

    val _name = MutableLiveData("")
    val _id = MutableLiveData("")
    private val name: String
        get() = _name.value?.trim() ?: ""
    private val id: String
        get() = _id.value?.trim() ?: ""

    val isValidName: LiveData<Boolean> = _name.map { name -> checkName(name) }
    val isValidId: LiveData<Boolean> = _id.map { id -> checkId(id) }
    private fun checkName(name: String) = Pattern.matches(REGEX_NAME_PATTERN, name)
    private fun checkId(id: String) = Pattern.matches(REGEX_ID_PATTERN, id)

    // 성별 viewmodel (step 4)

    val _gender = MutableLiveData("")
    val gender: String
        get() = _gender.value ?: ""

    fun selectGender(gender: String) {
        _gender.value = gender
    }

    // 친구 추가 viewmodel (step 5)

    var kakaoId: String = ""
    var email: String = ""
    var profileImg: String = ""

    private val _profile = MutableLiveData("")
    val profile: String
        get() = _profile.value ?: ""

    private val _friendState = MutableLiveData<FriendList>()
    val friendState: LiveData<FriendList> = _friendState

    var kakaoFriendList: List<String> = listOf()
    var selectedFriendIdList: List<Long> = listOf()
    var selectedFriendCount: MutableLiveData<Int> = MutableLiveData(0)

    private var currentFriendPage: Int = -1
    private var isFriendPagingFinish = false
    private var totalFriendPage = Int.MAX_VALUE

    fun addFriendList(friendGroup: FriendGroup) {
        viewModelScope.launch {
            if (isFriendPagingFinish) return@launch
            runCatching {
                onboardingRepository.postToGetFriendList(
                    RequestOnboardingListModel(friendGroup.friendIdList, friendGroup.groupId),
                    ++currentFriendPage,
                )
            }.onSuccess { friendList ->
                friendList ?: return@launch
                totalFriendPage = ceil((friendList.totalCount * 0.1)).toInt() - 1
                if (totalFriendPage == currentFriendPage) isFriendPagingFinish = true
                _friendState.value = friendList
            }.onFailure {
                Timber.e(it.message)
            }
        }
    }

    // 추천인 코드 viewmodel (step 6)

    val _code = MutableLiveData("")
    val code: String
        get() = _code.value?.trim() ?: ""

    private val _recommendId = MutableLiveData("")
    val recommendId: String
        get() = _recommendId.value ?: ""

    fun getValidYelloId() {
        viewModelScope.launch {
            onboardingRepository.getValidYelloId(yelloId = id)
                .onSuccess { isValid ->
                    Timber.d("GET VALID YELLO ID SUCCESS : $isValid")
                    if (isValid == null) {
                        _getValidYelloId.value = UiState.Empty
                        return@launch
                    }

                    _getValidYelloId.value = UiState.Success(isValid)
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("GET VALID YELLO ID FAILURE : $t")
                        _getValidYelloId.value = UiState.Failure(t.code().toString())
                        return@launch
                    }
                    Timber.e("GET VALID YELLO ID ERROR : $t")
                }
        }
    }

    // 앱 시작 회원가입 viewmodel (step 7)

    private val _postSignupState = MutableLiveData<UiState<UserInfo>>()
    val postSignupState: LiveData<UiState<UserInfo>> get() = _postSignupState

    private val _getValidYelloId = MutableLiveData<UiState<Boolean>>()
    val getValidYelloId: LiveData<UiState<Boolean>> get() = _getValidYelloId

    fun validYellIdLoading() {
        _getValidYelloId.value = UiState.Loading
    }

    fun postSignup() {
        viewModelScope.launch {
            val signupInfo = SignupInfo(
                kakaoId = kakaoId,
                email = email,
                profileImg = profileImg,
                groupId = groupId,
                studentId = studentId,
                name = name,
                yelloId = id,
                gender = gender,
                friendList = selectedFriendIdList,
                recommendId = code,
            )
            onboardingRepository.postSignup(signupInfo)
                .onSuccess { userInfo ->
                    Timber.d("POST SIGN UP SUCCESS : $userInfo")
                    if (userInfo == null) {
                        _postSignupState.value = UiState.Empty
                        return@launch
                    }
                    authRepository.setAutoLogin(userInfo.accessToken, userInfo.refreshToken)
                    authRepository.setYelloId(userInfo.yelloId)
                    _postSignupState.value = UiState.Success(userInfo)
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("POST SIGN UP FAILURE : $t")
                        _postSignupState.value = UiState.Failure(t.code().toString())
                        return@launch
                    }
                    Timber.e("POST SIGN UP ERROR : $t")
                }
        }
    }

    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage
    fun navigateToNextPage() {
        _currentPage.value = currentPage.value?.plus(1)
    }

    fun navigateToBackPage() {
        _currentPage.value = currentPage.value?.minus(1)
    }

    companion object {
        private const val REGEX_NAME_PATTERN = "^([ㄱ-ㅎㅏ-ㅣ가-힣]*)\$"
        private const val REGEX_ID_PATTERN = "^([A-Za-z0-9_.]*)\$"
    }
}
