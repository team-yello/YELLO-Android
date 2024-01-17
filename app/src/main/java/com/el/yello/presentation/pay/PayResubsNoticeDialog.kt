package com.el.yello.presentation.pay

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.FragmentNoticeResubscribeBinding
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PayResubsNoticeDialog :
    BindingDialogFragment<FragmentNoticeResubscribeBinding>(R.layout.fragment_notice_resubscribe) {

    private val viewModel by activityViewModels<PayViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserSubsInfoStateFromServer()
        observeSubsNeededState()
        setNoticeBtnCLickListener()
    }

    override fun onResume() {
        super.onResume()
        showDialogFullScreen()
    }

    private fun observeSubsNeededState() {
        viewModel.getUserSubsInfoState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    // TODO : 서버 url 변경 시 cancel 확인
                    if (state.data?.subscribe.toString() == "cancel") {
                        view?.isVisible = true
                        binding.tvResubscribeExpiredDate.text = state.data?.expiredDate
                    }
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Empty -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Loading -> {
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showDialogFullScreen() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    private fun setNoticeBtnCLickListener() {
        binding.btnNoticeQuit.setOnSingleClickListener {
            dismiss()
        }
        binding.btnYelloplusSubscribe.setOnSingleClickListener {
            // TODO : 계속 옐로 플러스 구독하기 뷰 이동
        }
    }
}
