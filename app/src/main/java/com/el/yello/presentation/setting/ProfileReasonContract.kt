package com.el.yello.presentation.setting

data class ProfileReasonState(
    val isLoading: Boolean = false,
    val etcText: String = "",
    val selectedReasonIndex: Int = -1,
    val isEnableButton: Boolean = false,
    val isShowProfileQuitDialog: Boolean = false,
    val isShowProfileQuitInviteFriendDialog: Boolean = false,
    val reasonList: List<String> = listOf(
        "앱에 아는 사람들이 없어서",
        "구독권과 열람권의 가격이 비싸서",
        "오류가 많아서",
        "재밌는 콘텐츠 또는 질문이 없어서",
        "포인트를 너무 적게 줘서",
        "내 정보를 삭제하고 싶어서",
        "다른 앱이 더 재밌어서",
        "기타"
    )
)

sealed class ProfileReasonSideEffect {
    data object NavigateBack : ProfileReasonSideEffect()
    data object NavigateToMainActivity : ProfileReasonSideEffect()
    data object SuccessKaKaoUnlink : ProfileReasonSideEffect()
    data class ShowToast(val message: String) : ProfileReasonSideEffect()
}