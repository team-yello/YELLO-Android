package com.el.yello.presentation.main.recommend

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import com.el.yello.BuildConfig
import com.el.yello.R
import com.el.yello.databinding.FragmentRecommendInviteDialogBinding
import com.el.yello.presentation.main.yello.dialog.UnlockDialogFragment.Companion.ARGS_YELLO_ID
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import timber.log.Timber

class RecommendInviteDialog :
    BindingDialogFragment<FragmentRecommendInviteDialogBinding>(R.layout.fragment_recommend_invite_dialog) {

    private lateinit var myYelloId: String
    private lateinit var linkText: String
    private var templateId: Long = 0

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.color.transparent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBundleArgs()
        setTemplateId()
        setRecommendId()
        initExitBtnListener()
        initKakaoInviteBtnListener()
        initLinkInviteBtnListener()
    }

    private fun getBundleArgs() {
        arguments ?: return
        myYelloId = arguments?.getString(ARGS_YELLO_ID) ?: ""
        linkText = LINK_TEXT.format(myYelloId)
    }

    private fun setRecommendId() {
        binding.tvRecommendDialogInviteId.text = myYelloId
    }

    private fun setTemplateId() {
        if (BuildConfig.DEBUG) {
            templateId = TEST_TEMPLATE_ID.toLong()
        } else {
            templateId = TEMPLATE_ID.toLong()
        }
    }

    private fun initExitBtnListener() {
        binding.btnInviteDialogExit.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun initKakaoInviteBtnListener() {
        binding.btnInviteKakao.setOnSingleClickListener {
            startKakaoInvite(requireContext())
        }
    }

    private fun initLinkInviteBtnListener() {
        binding.btnInviteLink.setOnSingleClickListener {
            val clipboardManager =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(CLIP_LABEL, linkText)
            clipboardManager.setPrimaryClip(clipData)
        }
    }

    private fun startKakaoInvite(context: Context) {
        // 카카오톡 설치 여부 확인
        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            // 앱으로 공유
            ShareClient.instance.shareCustom(
                context,
                templateId,
                mapOf("KEY" to myYelloId),
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
                return
            } catch (error: UnsupportedOperationException) {
                Timber.tag(TAG_SHARE).e(error, getString(R.string.invite_error_browser))
            }

            // 2. CustomTabsServiceConnection 미지원 브라우저 - 네이버 앱 등
            try {
                KakaoCustomTabsClient.open(context, sharerUrl)
                return
            } catch (error: ActivityNotFoundException) {
                Timber.tag(TAG_SHARE).e(error, getString(R.string.invite_error_browser))
            }
        }
    }

    companion object {
        const val TAG_SHARE = "recommendInvite"

        const val TEMPLATE_ID = 95890
        const val TEST_TEMPLATE_ID = 96906

        const val LINK_TEXT = "추천인코드: %s\n" + "우리 같이 YELL:O 해요!\n" + "(여기에는 다운로드 링크)"
        const val CLIP_LABEL = "label"

        @JvmStatic
        fun newInstance(yelloId: String) = RecommendInviteDialog().apply {
            val args = bundleOf(
                ARGS_YELLO_ID to yelloId,
            )
            arguments = args
        }
    }
}
