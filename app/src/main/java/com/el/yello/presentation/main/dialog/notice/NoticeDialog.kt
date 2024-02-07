package com.el.yello.presentation.main.dialog.notice

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.el.yello.R
import com.el.yello.databinding.FragmentNoticeDialogBinding
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NoticeDialog :
    BindingDialogFragment<FragmentNoticeDialogBinding>(R.layout.fragment_notice_dialog) {
    private val viewModel by viewModels<NoticeViewModel>()

    private lateinit var imageUrl: String
    private lateinit var redirectUrl: String

    override fun onStart() {
        super.onStart()

        initContainerLayoutTransparent()
    }

    private fun initContainerLayoutTransparent() {
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
        initNoticeImageView()
        initDoNotSeeItAgainBtnClickListener()
        initCloseBtnClickListener()
        setupIsNoticeDisabled()
    }

    private fun getBundleArgs() {
        imageUrl = arguments?.getString(ARGS_IMAGE_URL) ?: return
        redirectUrl = arguments?.getString(ARGS_REDIRECT_URL) ?: return
    }

    private fun initNoticeImageView() {
        with(binding.ivNoticeImg) {
            load(imageUrl)

            if (redirectUrl.isBlank()) return
            setOnSingleClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl)))
            }
        }
    }

    private fun initDoNotSeeItAgainBtnClickListener() {
        binding.btnNoticeDoNotSeeItAgain.setOnSingleClickListener {
            viewModel.switchNoticeDisabledState()
        }
        binding.icNoticeDoNotSeeItAgain.setOnSingleClickListener {
            viewModel.switchNoticeDisabledState()
        }
        binding.tvNoticeDoNotSeeItAgain.setOnSingleClickListener {
            viewModel.switchNoticeDisabledState()
        }
    }

    private fun setupIsNoticeDisabled() {
        viewModel.isNoticeDisabled.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { isNoticeDisabled ->
                binding.icNoticeDoNotSeeItAgain.setImageResource(
                    if (isNoticeDisabled) R.drawable.ic_notice_check else R.drawable.ic_notice_uncheck,
                )
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initCloseBtnClickListener() {
        binding.tvNoticeClose.setOnSingleClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setDisabledNotice(imageUrl)
    }

    companion object {
        private const val ARGS_IMAGE_URL = "IMAGE_URL"
        private const val ARGS_REDIRECT_URL = "REDIRECT_URL"

        @JvmStatic
        fun newInstance(
            imageUrl: String,
            redirectUrl: String,
        ) = NoticeDialog().apply {
            arguments = bundleOf(
                ARGS_IMAGE_URL to imageUrl,
                ARGS_REDIRECT_URL to redirectUrl,
            )
        }
    }
}
