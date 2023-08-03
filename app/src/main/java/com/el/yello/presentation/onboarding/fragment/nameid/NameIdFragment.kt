package com.el.yello.presentation.onboarding.fragment.nameid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentNameIdBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import java.util.Timer
import kotlin.concurrent.timer

class NameIdFragment : BindingFragment<FragmentNameIdBinding>(R.layout.fragment_name_id) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    var timer: Timer? = null
    var deltaTime = 32
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setDeleteBtnClickListener()
        setConfirmBtnClickListener()
        setupGetValidYelloId()
        ProgressBarTimerFun()
    }
    private fun ProgressBarTimerFun() {
        binding.nameidProgressbar.progress = 32
        timer?.cancel()
        timer = Timer()
        timer = timer(period = 8, initialDelay = 300) {
            if (deltaTime > 48) cancel()
            binding.nameidProgressbar.setProgress(++deltaTime)
            println(binding.nameidProgressbar.progress)
        }
    }
    private fun setConfirmBtnClickListener() {
        binding.btnNameidNext.setOnSingleClickListener {
            viewModel.getValidYelloId()
        }
        binding.btnNameidBackBtn.setOnSingleClickListener {
            viewModel.navigateToBackPage()
        }
    }

    private fun setDeleteBtnClickListener() {
        binding.btnNameDelete.setOnSingleClickListener {
            binding.etName.text.clear()
        }
        binding.btnIdDelete.setOnClickListener {
            binding.etId.text.clear()
        }
    }

    private fun setupGetValidYelloId() {
        viewModel.getValidYelloId.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data) {
                        viewModel.navigateToNextPage()
                        return@observe
                    }
                    initIdEditTextViewError()
                }

                is UiState.Failure -> {
                    if (state.msg == NOT_FOUND) {
                        viewModel.navigateToNextPage()
                        return@observe
                    }
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Loading -> {}
                is UiState.Empty -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }
            }
        }
    }

    private fun initIdEditTextViewError() {
        binding.etId.setBackgroundResource(R.drawable.shape_fill_red20_line_semantic_status_red500_rect_8)
        binding.btnIdDelete.setBackgroundResource(R.drawable.ic_onboarding_delete_red)
        binding.tvIdError.text = getString(R.string.onboarding_code_duplicate_msg)
        binding.tvIdError.setTextColor(resources.getColor(R.color.semantic_red_500))
    }

    companion object {
        const val NOT_FOUND = "404"
    }
}
