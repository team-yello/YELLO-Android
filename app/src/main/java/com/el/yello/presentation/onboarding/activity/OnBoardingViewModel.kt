package com.el.yello.presentation.onboarding.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.entity.onboarding.AddFriendListModel
import com.example.domain.entity.onboarding.GroupList
import com.example.domain.entity.onboarding.RequestAddFriendModel
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

    var currentpercent = 20
    fun plusCurrentPercent() {
        currentpercent += 20
    }

    fun minusCurrentPercent() {
        currentpercent -= 20
    }

    var isFirstUser: Boolean = false
    fun resetGetVaildYelloId() {
        _getValidYelloId.value = UiState.Loading
    }

    // 학력 선택
    val studentType = MutableLiveData("")

    // 고등학생
    val highSchoolText = MutableLiveData("")
    val gradeText = MutableLiveData("")
    val groupText = MutableLiveData<Int>()
    val schoolText = MutableLiveData("")

    // 대학생
    val school: String get() = schoolText.value?.trim() ?: ""
    val departmentText = MutableLiveData("")
    val studentIdText = MutableLiveData<Int>()
    val studentId: Int get() = requireNotNull(studentIdText.value)
    private val _groupId = MutableLiveData<Long>()
    val groupId: Long get() = requireNotNull(_groupId.value)

    val nameText = MutableLiveData("")

    val idText = MutableLiveData("")
    val id: String get() = idText.value?.trim() ?: ""

    val isValidName: LiveData<Boolean> = nameText.map { name -> checkName(name) }
    val isValidId: LiveData<Boolean> = idText.map { id -> checkId(id) }
    private fun checkName(name: String) = Pattern.matches(REGEX_NAME_PATTERN, name)
    private fun checkId(id: String) = Pattern.matches(REGEX_ID_PATTERN, id)

    // 공통
    val genderText = MutableLiveData("")

    val codeText = MutableLiveData("")
    fun isCodeTextEmpty(): Boolean {
        return codeText.value.isNullOrEmpty()
    }

    private val _groupResult: MutableLiveData<List<Int>> = MutableLiveData()
    val groupResult: LiveData<List<Int>> = _groupResult

    private val _schoolData = MutableLiveData<UiState<SchoolList>>()
    val schoolData: MutableLiveData<UiState<SchoolList>> = _schoolData

    private val _departmentData = MutableLiveData<UiState<GroupList>>()
    val departmentData: MutableLiveData<UiState<GroupList>> = _departmentData

    private val _studentIdResult: MutableLiveData<List<Int>> = MutableLiveData()
    val studentIdResult: LiveData<List<Int>> = _studentIdResult

    private val _friendListState = MutableLiveData<UiState<AddFriendListModel>>()
    val friendListState: LiveData<UiState<AddFriendListModel>> = _friendListState

    var selectedFriendIdList: List<Long> = listOf()
    var selectedFriendCount: MutableLiveData<Int> = MutableLiveData(0)

    private val _getValidYelloId = MutableLiveData<UiState<Boolean>>()
    val getValidYelloId: LiveData<UiState<Boolean>> get() = _getValidYelloId

    private val _postSignupState = MutableLiveData<UiState<UserInfo>>()
    val postSignupState: LiveData<UiState<UserInfo>> get() = _postSignupState

    // 학력 선택
    fun selectStudentType(student: String) {
        studentType.value = student
    }

    // 고등학생
    fun selectGrade(grade: String?) {
        gradeText.value = grade ?: ""
    }

    fun setGroup(group: Int) {
        groupText.value = group
    }

    fun addGroup() {
        val studentGroupList =
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
        _groupResult.value = studentGroupList
    }

    // 대학생
    fun setSchool(university: String) {
        schoolText.value = university
    }

    fun clearSchoolData() {
        _schoolData.value = UiState.Success(SchoolList(0, emptyList()))
    }

    fun setStudentId(studentId: Int) {
        studentIdText.value = studentId
    }

    fun setGroupInfo(department: String, groupId: Long) {
        departmentText.value = department
        _groupId.value = groupId
    }

    fun addStudentId() {
        val studentIdList = listOf(15, 16, 17, 18, 19, 20, 21, 22, 23)
        _studentIdResult.value = studentIdList
    }

    fun clearDepartmentData() {
        _departmentData.value = UiState.Success(GroupList(0, emptyList()))
    }

    // 공통
    fun selectGender(gender: String) {
        genderText.value = gender
    }

    private var currentFriendOffset = -100
    private var currentFriendPage = -1
    private var isFriendPagingFinish = false
    private var totalFriendPage = Int.MAX_VALUE
    private var isFirstFriendsListPage: Boolean = true

    fun initFriendPagingVariable() {
        selectedFriendCount.value = 0
        isFirstFriendsListPage = true
        currentFriendOffset = -100
        currentFriendPage = -1
        isFriendPagingFinish = false
        totalFriendPage = Int.MAX_VALUE
    }

    fun validYellIdLoading() {
        _getValidYelloId.value = UiState.Loading
    }

    // 서버 통신 - 학교 찾기
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
                _schoolData.value = when {
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

    // 서버 통신 - 학과 찾기
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
                _departmentData.value = when {
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
            limit = 100,
        ) { friends, error ->
            if (error != null) {
                Timber.e(error, "카카오톡 친구목록 가져오기 실패")
            } else if (friends != null) {
                totalFriendPage = ceil((friends.totalCount * 0.01)).toInt() - 1
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
        if (isFirstFriendsListPage) {
            _friendListState.value = UiState.Loading
            isFirstFriendsListPage = false
        }
        viewModelScope.launch {
            runCatching {
                onboardingRepository.postToGetFriendList(
                    RequestAddFriendModel(friendKakaoId, groupId),
                    0,
                )
            }.onSuccess { friendList ->
                friendList ?: return@launch
                _friendListState.value = UiState.Success(friendList)
            }.onFailure {
                _friendListState.value = UiState.Failure(it.message.toString())
            }
        }
    }

    // 서버 통신 - 옐로아이디
    fun getValidYelloId(unknownId: String) {
        viewModelScope.launch {
            onboardingRepository.getValidYelloId(yelloId = unknownId).onSuccess { isValid ->
                Timber.d("GET VALID YELLO ID SUCCESS : $isValid")
                if (isValid == null) {
                    _getValidYelloId.value = UiState.Empty
                    return@launch
                }
                _getValidYelloId.value = UiState.Success(isValid)
            }.onFailure { t ->
                if (t is HttpException) {
                    Timber.e("GET VALID YELLO ID FAILURE : $t")
                    _getValidYelloId.value = UiState.Failure(t.code().toString())
                    return@launch
                }
                Timber.e("GET VALID YELLO ID ERROR : $t")
            }
        }
    }

    var kakaoId: String = ""
    var email: String = ""
    var profileImg: String = ""
    var name: String = ""
    var gender: String = ""
    fun postSignup() {
        viewModelScope.launch {
            val deviceToken = authRepository.getDeviceToken()
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
                recommendId = codeText.value,
                deviceToken = deviceToken,
            )
            onboardingRepository.postSignup(signupInfo).onSuccess { userInfo ->
                Timber.d("POST SIGN UP SUCCESS : $userInfo")
                if (userInfo == null) {
                    _postSignupState.value = UiState.Empty
                    return@launch
                }
                authRepository.setAutoLogin(userInfo.accessToken, userInfo.refreshToken)
                authRepository.setYelloId(userInfo.yelloId)
                _postSignupState.value = UiState.Success(userInfo)
            }.onFailure { t ->
                if (t is HttpException) {
                    Timber.e("POST SIGN UP FAILURE : $t")
                    _postSignupState.value = UiState.Failure(t.code().toString())
                    return@launch
                }
                Timber.e("POST SIGN UP ERROR : $t")
            }
        }
        AmplitudeUtils.updateUserProperties("user_sex", gender)
    }
    companion object {
        private const val REGEX_NAME_PATTERN = "^([가-힣]*)\$"
        private const val REGEX_ID_PATTERN = "^([A-Za-z0-9_.]*)\$"
    }
}
