package com.yello.presentation.main.yello.dialog

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.yello.R
import com.yello.databinding.FragmentUnlockDialogBinding
import com.yello.presentation.main.recommend.RecommendInviteDialog
import com.yello.util.context.yelloSnackbar
import timber.log.Timber

class UnlockDialogFragment :
    BindingDialogFragment<FragmentUnlockDialogBinding>(R.layout.fragment_unlock_dialog) {
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawableResource(R.color.transparent)
        }
    }

    // 사용자 정의 템플릿 ID & 공유할 url
    // TODO: 추천인 아이디 설정 & 링크 생기면 넣기
    private val templateId = 95890.toLong()
    private val url = "http://naver.com"
    private val myYelloId: String = "sangho.kk"
    private val linkText: String = "추천인코드: {$myYelloId}\n" +
            "우리 같이 YELL:O 해요!\n" +
            "(여기에는 다운로드 링크)"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecommendId()
        initExitButton()
        initKakaoInviteButton()
        initLinkInviteButton()
    }

    private fun setRecommendId() {
        binding.tvUnlockInviteId.text = myYelloId
    }

    private fun initExitButton() {
        binding.btnUnlockExit.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun initKakaoInviteButton() {
        binding.btnUnlockInviteKakao.setOnSingleClickListener {
            startKakaoInvite(requireContext())
        }
    }

    private fun initLinkInviteButton() {
        binding.btnUnlockInviteLink.setOnSingleClickListener {
            val clipboardManager =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("label", linkText)
            clipboardManager.setPrimaryClip(clipData)
        }
    }

    private fun startKakaoInvite(context: Context) {
        // 카카오톡 설치여부 확인
        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            // 카카오톡으로 공유
            ShareClient.instance.shareCustom(
                context,
                templateId,
                mapOf("KEY" to myYelloId)
            ) { sharingResult, error ->
                if (error != null) {
                    Timber.tag(TAG_SHARE).e(error, getString(R.string.invite_error_kakao))
                } else if (sharingResult != null) {
                    startActivity(sharingResult.intent)
                }
            }
        } else {
            // 웹으로 공유
            val sharerUrl =
                WebSharerClient.instance.makeCustomUrl(templateId)

            // 1. CustomTabsServiceConnection 지원 브라우저 - Chrome, 삼성 인터넷 등
            try {
                KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
            } catch (error: UnsupportedOperationException) {
                Timber.tag(TAG_SHARE).e(error, getString(R.string.invite_error_browser))
            }

            // 2. CustomTabsServiceConnection 미지원 브라우저 - 네이버 앱
            try {
                KakaoCustomTabsClient.open(context, sharerUrl)
            } catch (error: ActivityNotFoundException) {
                Timber.tag(TAG_SHARE).e(error, getString(R.string.invite_error_browser))
            }
        }
    }

    private companion object {
        const val TAG_SHARE = "UNLOCK"

        @JvmStatic
        fun newInstance() = UnlockDialogFragment()
    }
}
