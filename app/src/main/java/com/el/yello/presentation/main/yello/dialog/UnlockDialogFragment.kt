package com.el.yello.presentation.main.yello.dialog

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
import com.el.yello.databinding.FragmentUnlockDialogBinding
import com.el.yello.presentation.main.recommend.RecommendInviteDialog
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingDialogFragment
import com.example.ui.fragment.toast
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import org.json.JSONObject
import timber.log.Timber

class UnlockDialogFragment :
    BindingDialogFragment<FragmentUnlockDialogBinding>(R.layout.fragment_unlock_dialog) {

    private lateinit var myYelloId: String
    private lateinit var previousScreen: String
    private lateinit var linkText: String
    private var templateId: Long = 0

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            dialog?.window?.apply {
                setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                )
            }
            setBackgroundDrawableResource(R.color.transparent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBundleArgs()
        setTemplateId()
        setRecommendId()
        initExitButton()
        initKakaoInviteButton()
        initLinkInviteButton()
    }

    private fun getBundleArgs() {
        arguments ?: return
        myYelloId = arguments?.getString(ARGS_YELLO_ID) ?: ""
        previousScreen = arguments?.getString(ARGS_PREVIOUS_SCREEN) ?: ""
        binding.yelloId = myYelloId
        linkText = RecommendInviteDialog.LINK_TEXT.format(myYelloId)
    }

    private fun setRecommendId() {
        binding.tvUnlockInviteSubtitle.text = myYelloId
    }

    private fun setTemplateId() {
        templateId = if (BuildConfig.DEBUG) {
            RecommendInviteDialog.TEST_TEMPLATE_ID.toLong()
        } else {
            RecommendInviteDialog.TEMPLATE_ID.toLong()
        }
    }

    private fun initExitButton() {
        binding.btnUnlockExit.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun initKakaoInviteButton() {
        binding.btnUnlockInviteKakao.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(
                "click_invite_kakao",
                JSONObject().put("invite_view", previousScreen),
            )
            startKakaoInvite(requireContext())
        }
    }

    private fun initLinkInviteButton() {
        binding.btnUnlockInviteLink.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(
                "click_invite_link",
                JSONObject().put("invite_view", previousScreen),
            )
            val clipboardManager =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(LABEL_UNLOCK_LINK_TEXT, linkText)
            clipboardManager.setPrimaryClip(clipData)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) toast(getString(R.string.invite_clipboard_msg))
        }
    }

    private fun startKakaoInvite(context: Context) {
        // 카카오톡 설치 시 카카오톡으로 공유
        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            ShareClient.instance.shareCustom(
                context,
                templateId,
                mapOf(KEY_YELLO_ID to myYelloId),
            ) { sharingResult, error ->
                if (error != null) {
                    Timber.tag(TAG_UNLOCK).e(error, getString(R.string.invite_error_kakao))
                    return@shareCustom
                }

                if (sharingResult != null) {
                    startActivity(sharingResult.intent)
                }
            }
            return
        }

        val sharerUrl = WebSharerClient.instance.makeCustomUrl(templateId)

        // 1. CustomTabsServiceConnection 지원 브라우저 - Chrome, 삼성 인터넷 등
        try {
            KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
            return
        } catch (error: UnsupportedOperationException) {
            Timber.tag(TAG_UNLOCK).e(error, getString(R.string.invite_error_browser))
        }

        // 2. CustomTabsServiceConnection 미지원 브라우저 - 네이버 앱
        try {
            KakaoCustomTabsClient.open(context, sharerUrl)
        } catch (error: ActivityNotFoundException) {
            Timber.tag(TAG_UNLOCK).e(error, getString(R.string.invite_error_browser))
        }
    }

    companion object {
        const val ARGS_YELLO_ID = "YELLO_ID"
        const val ARGS_PREVIOUS_SCREEN = "PREVIOUS_SCREEN"

        const val TAG_UNLOCK = "UNLOCK"

        private const val KEY_YELLO_ID = "KEY"
        private const val LABEL_UNLOCK_LINK_TEXT = "UNLOCK_LINK"

        @JvmStatic
        fun newInstance(yelloId: String, previousScreen: String) = UnlockDialogFragment().apply {
            val args = bundleOf(
                ARGS_YELLO_ID to yelloId,
                ARGS_PREVIOUS_SCREEN to previousScreen,
            )
            arguments = args
        }
    }
}
