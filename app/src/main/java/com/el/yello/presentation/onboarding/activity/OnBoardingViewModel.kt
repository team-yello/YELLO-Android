package com.el.yello.presentation.onboarding.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RequestOnboardingListModel
import com.example.domain.entity.onboarding.FriendList
import com.example.domain.entity.onboarding.GroupList
import com.example.domain.entity.onboarding.SchoolList
import com.example.domain.entity.onboarding.SignupInfo
import com.example.domain.entity.onboarding.UserInfo
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.OnboardingRepository
import com.example.ui.view.UiState
import com.kakao.sdk.talk.TalkApiClient
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
    private val _postSignupState = MutableLiveData<UiState<UserInfo>>()
    val postSignupState: LiveData<UiState<UserInfo>>
        get() = _postSignupState

    private val _getValidYelloId = MutableLiveData<UiState<Boolean>>()
    val getValidYelloId: LiveData<UiState<Boolean>>
        get() = _getValidYelloId

    private val _schoolData = MutableLiveData<UiState<SchoolList>>()
    val schoolData: MutableLiveData<UiState<SchoolList>> = _schoolData

    private val _departmentData = MutableLiveData<UiState<GroupList>>()
    val departmentData: MutableLiveData<UiState<GroupList>> = _departmentData

    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

    var schoolPage = -1
    private var isSchoolPagingFinish = false
    private var totalSchoolPage = Integer.MAX_VALUE

    var departmentPage = -1
    private var isDepartmentPagingFinish = false
    private var totalDepartmentPage = Integer.MAX_VALUE

    val _school = MutableLiveData("")
    val school: String
        get() = _school.value?.trim() ?: ""

    var kakaoId: String = ""
    var email: String = ""
    var profileImg: String = ""

    private val _groupId = MutableLiveData<Long>()
    val groupId: Long
        get() = requireNotNull(_groupId.value)

    val _department = MutableLiveData("")
    private val department: String
        get() = _department.value?.trim() ?: ""

    val _studentId = MutableLiveData<Int>()
    private val studentId: Int
        get() = requireNotNull(_studentId.value)

    val _name = MutableLiveData("")
    private val name: String
        get() = _name.value?.trim() ?: ""

    val _id = MutableLiveData("")
    private val id: String
        get() = _id.value?.trim() ?: ""

    val _gender = MutableLiveData("")
    val gender: String
        get() = _gender.value ?: ""

    val _code = MutableLiveData("")
    val code: String
        get() = _code.value?.trim() ?: ""

    private val _profile = MutableLiveData("")
    val profile: String
        get() = _profile.value ?: ""

    private val _recommendId = MutableLiveData("")
    val recommendId: String
        get() = _recommendId.value ?: ""

    val isValidSchool: LiveData<Boolean> = _school.map { school -> checkValidSchool(school) }
    val isEmptyDepartment: LiveData<Boolean> =
        _department.map { department -> this.checkEmpty(department) }

    val isValidName: LiveData<Boolean> = _name.map { name -> checkName(name) }
    val isValidId: LiveData<Boolean> = _id.map { id -> checkId(id) }

    val isEmptyCode: LiveData<Boolean> = _code.map { code -> checkEmpty(code) }

    private val _studentIdResult: MutableLiveData<List<Int>> = MutableLiveData()
    val studentIdResult: LiveData<List<Int>> = _studentIdResult

    private val _friendState = MutableLiveData<FriendList>()
    val friendState: LiveData<FriendList> = _friendState

    var selectedFriendIdList: List<Long> = listOf()
    var selectedFriendCount: MutableLiveData<Int> = MutableLiveData(0)

    private var currentFriendOffset = -100
    private var currentFriendPage = -1
    private var isFriendPagingFinish = false
    private var totalFriendPage = Int.MAX_VALUE

    fun initFriendPagingVariable() {
        currentFriendOffset = -100
        currentFriendPage = -1
        isFriendPagingFinish = false
        totalFriendPage = Int.MAX_VALUE
    }

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
        // if (isDepartmentPagingFinish) return
        viewModelScope.launch {
            _departmentData.value = UiState.Loading
            onboardingRepository.getGroupList(
                school,
                search,
                0,
//                ++departmentPage,
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

    // 서버 통신 - 카카오 리스트 통신 후 친구 리스트 추가 서버 통신 진행
    fun addListWithKakaoIdList() {
        if (isFriendPagingFinish) return
        currentFriendOffset += 100
        currentFriendPage += 1
        TalkApiClient.instance.friends(
            offset = currentFriendOffset,
            limit = 100
        ) { friends, error ->
            if (error != null) {
                Timber.e(error, "카카오톡 친구목록 가져오기 실패")
            } else if (friends != null) {
                totalFriendPage = ceil((friends.totalCount * 0.1)).toInt() - 1
                if (totalFriendPage == currentFriendPage) isFriendPagingFinish = true
                val friendIdList: List<String> =
                    friends.elements?.map { friend -> friend.id.toString() } ?: listOf()
                getListFromServer(friendIdList, groupId)
            } else {
                Timber.d("연동 가능한 카카오톡 친구 없음")
            }
        }
    }

    // 서버 통신 - 추천 친구 리스트 추가
    private fun getListFromServer(friendKakaoId: List<String>, groupId: Long) {
        viewModelScope.launch {
            runCatching {
                onboardingRepository.postToGetFriendList(
                    RequestOnboardingListModel(friendKakaoId, groupId),
                    0
                )
            }.onSuccess { friendList ->
                friendList ?: return@launch
                _friendState.value = friendList
            }.onFailure {
                Timber.e(it.message)
            }
        }
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
                recommendId = recommendId,
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

    fun setSchool(school: String) {
        _school.value = school
    }

    fun setGroupInfo(department: String, groupId: Long) {
        _department.value = department
        _groupId.value = groupId
    }

    fun setStudentId(studentId: Int) {
        _studentId.value = studentId
    }

    fun selectGender(gender: String) {
        _gender.value = gender
    }

    fun clearSchoolData() {
        _schoolData.value = UiState.Success(SchoolList(0, emptyList()))
    }

    fun clearDepartmentData() {
        _departmentData.value = UiState.Success(GroupList(0, emptyList()))
    }

    private fun checkValidSchool(school: String): Boolean {
        return school.isNotBlank()
    }

    private fun checkEmpty(input: String) = input.isBlank()

    private fun checkName(name: String) = Pattern.matches(REGEX_NAME_PATTERN, name)

    private fun checkId(id: String) = Pattern.matches(REGEX_ID_PATTERN, id)

    fun navigateToNextPage() {
        _currentPage.value = currentPage.value?.plus(1)
    }

    fun navigateToBackPage() {
        _currentPage.value = currentPage.value?.minus(1)
    }

    fun addStudentId() {
        val mockList = listOf(15, 16, 17, 18, 19, 20, 21, 22, 23)
        _studentIdResult.value = mockList
    }

    fun validYellIdLoading() {
        _getValidYelloId.value = UiState.Loading
    }

    companion object {
        private const val REGEX_NAME_PATTERN = "^([ㄱ-ㅎㅏ-ㅣ가-힣]*)\$"
        private const val REGEX_ID_PATTERN = "^([A-Za-z0-9_.]*)\$"
    }
}
