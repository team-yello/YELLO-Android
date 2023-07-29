package com.el.yello.presentation.main.yello.dialog

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import com.el.yello.R
import com.el.yello.databinding.FragmentUnlockDialogBinding
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import timber.log.Timber

class UnlockDialogFragment :
    BindingDialogFragment<FragmentUnlockDialogBinding>(R.layout.fragment_unlock_dialog) {
    private lateinit var myYelloId: String
    private lateinit var linkText: String

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
        initExitButton()
        initKakaoInviteButton()
        initLinkInviteButton()
    }

    private fun getBundleArgs() {
        arguments ?: return
        myYelloId = arguments?.getString(ARGS_YELLO_ID) ?: ""
        binding.yelloId = myYelloId
        linkText = getString(R.string.unlock_link_text, myYelloId)
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
            val clipData = ClipData.newPlainText(LABEL_UNLOCK_LINK_TEXT, linkText)
            clipboardManager.setPrimaryClip(clipData)
        }
    }

    private fun startKakaoInvite(context: Context) {
        // 카카오톡 설치 시 카카오톡으로 공유
        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            ShareClient.instance.shareCustom(
                context,
                UNLOCK_TEMPLATE_ID,
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

        val sharerUrl = WebSharerClient.instance.makeCustomUrl(UNLOCK_TEMPLATE_ID)

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

        const val TAG_UNLOCK = "UNLOCK"

        private const val UNLOCK_TEMPLATE_ID = 95890.toLong()
        private const val KEY_YELLO_ID = "YELLO_ID"
        private const val LABEL_UNLOCK_LINK_TEXT = "UNLOCK_LINK"

        @JvmStatic
        fun newInstance(yelloId: String) = UnlockDialogFragment().apply {
            val args = bundleOf(
                ARGS_YELLO_ID to yelloId,
            )
            arguments = args
        }
    }
}
