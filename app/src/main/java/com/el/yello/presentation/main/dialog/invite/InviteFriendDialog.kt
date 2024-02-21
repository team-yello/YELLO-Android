package com.el.yello.presentation.main.dialog.invite

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import com.el.yello.BuildConfig
import com.el.yello.R
import com.el.yello.databinding.FragmentInviteFriendDialogBinding
import com.el.yello.util.AmplitudeManager
import com.example.ui.base.BindingDialogFragment
import com.example.ui.extension.toast
import com.example.ui.extension.setOnSingleClickListener
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import org.json.JSONObject
import timber.log.Timber

class InviteFriendDialog :
    BindingDialogFragment<FragmentInviteFriendDialogBinding>(R.layout.fragment_invite_friend_dialog) {

    private lateinit var myYelloId: String
    private lateinit var previousScreen: String
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
        previousScreen = arguments?.getString(ARGS_PREVIOUS_SCREEN) ?: ""
        linkText = LINK_TEXT.format(myYelloId)
    }

    private fun setRecommendId() {
        binding.tvRecommendDialogInviteId.text = myYelloId
    }

    private fun setTemplateId() {
        templateId = if (BuildConfig.DEBUG) {
            TEST_TEMPLATE_ID.toLong()
        } else {
            TEMPLATE_ID.toLong()
        }
    }

    private fun initExitBtnListener() {
        binding.btnInviteDialogExit.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun initKakaoInviteBtnListener() {
        binding.btnInviteKakao.setOnSingleClickListener {
            AmplitudeManager.trackEventWithProperties(
                "click_invite_kakao",
                JSONObject().put("invite_view", previousScreen),
            )
            startKakaoInvite(requireContext())
        }
    }

    private fun initLinkInviteBtnListener() {
        binding.btnInviteLink.setOnSingleClickListener {
            AmplitudeManager.trackEventWithProperties(
                "click_invite_link",
                JSONObject().put("invite_view", previousScreen),
            )
            val clipboardManager =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(CLIP_LABEL, linkText)
            clipboardManager.setPrimaryClip(clipData)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) toast(getString(R.string.invite_clipboard_msg))
        }
    }

    private fun startKakaoInvite(context: Context) {
        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            shareKakaoWithApp(context)
        } else {
            shareKakaoWithWeb(context)
        }
    }

    private fun shareKakaoWithApp(context: Context) {
        ShareClient.instance.shareCustom(
            context,
            templateId,
            mapOf(KEY_YELLO_ID to myYelloId),
        ) { sharingResult, error ->
            if (error != null) {
                Timber.tag(TAG_SHARE).e(error, getString(R.string.invite_error_kakao))
            } else if (sharingResult != null) {
                startActivity(sharingResult.intent)
            }
        }
    }

    private fun shareKakaoWithWeb(context: Context) {
        val sharerUrl = WebSharerClient.instance.makeCustomUrl(templateId)
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

    companion object {
        const val TAG_SHARE = "recommendInvite"

        const val ARGS_PREVIOUS_SCREEN = "PREVIOUS_SCREEN"
        const val ARGS_YELLO_ID = "YELLO_ID"

        const val TEMPLATE_ID = 95890
        const val TEST_TEMPLATE_ID = 96906

        const val LINK_TEXT = "추천인코드: %s\n" +
            "우리 같이 YELL:O 해요!\n" +
            "Android: https://play.google.com/store/apps/details?id=com.el.yello&hl=ko&gl=KR\n" +
            "iOS: https://apps.apple.com/app/id6451451050"

        const val CLIP_LABEL = "RECOMMEND_LINK"
        private const val KEY_YELLO_ID = "KEY"

        @JvmStatic
        fun newInstance(yelloId: String, previousScreen: String) = InviteFriendDialog().apply {
            val args = bundleOf(
                ARGS_YELLO_ID to yelloId,
                ARGS_PREVIOUS_SCREEN to previousScreen,
            )
            arguments = args
        }
    }
}
