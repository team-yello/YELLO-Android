package com.el.yello.presentation.onboarding.fragment.code

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.databinding.FragmentCodeBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import java.util.Timer
import kotlin.concurrent.timer

class CodeFragment : BindingFragment<FragmentCodeBinding>(R.layout.fragment_code) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    var timer: Timer? = null
    var deltaTime = 64
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setConfirmBtnCLickListener()
        setDeleteCodeBtnClickListener()
        setupPostSignupState()
        viewModel.validYellIdLoading()
        progressBarTimerFun()
    }

    private fun progressBarTimerFun() {
        binding.codeProgressbar.progress = 64
        timer?.cancel()
        timer = Timer()
        timer = timer(period = 8, initialDelay = 300) {
            if (deltaTime > 80) cancel()
            binding.codeProgressbar.setProgress(++deltaTime)
            println(binding.codeProgressbar.progress)
        }
    }

    override fun onResume() {
        super.onResume()
        setupGetValidYelloIdState()
    }

    private fun setConfirmBtnCLickListener() {
        binding.btnCodeSkip.setOnClickListener {
            viewModel.postSignup()
            findNavController().navigate(R.id.action_codeFragment_to_startAppFragment)
        }
        binding.btnCodeNext.setOnSingleClickListener {
            viewModel.getValidYelloId(viewModel.codeText.value.toString())
            findNavController().navigate(R.id.action_codeFragment_to_startAppFragment)
        }
    }

    private fun setupPostSignupState() {
        viewModel.postSignupState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    findNavController().navigate(R.id.action_codeFragment_to_startAppFragment)
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    private fun setupGetValidYelloIdState() {
        viewModel.getValidYelloId.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (!state.data) {
                        initIdEditTextViewError()
                        return@observe
                    }
                    viewModel.postSignup()
                }

                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }
            }
        }
    }

    private fun setDeleteCodeBtnClickListener() {
        binding.ivCodeDelete.setOnClickListener {
            binding.etCode.setText("")
        }
    }

    private fun initIdEditTextViewError() {
        binding.etCode.setBackgroundResource(R.drawable.shape_fill_red20_line_semantic_status_red500_rect_8)
        binding.ivCodeDelete.setBackgroundResource(R.drawable.ic_onboarding_delete_red)
        binding.tvCodeHint.text = getString(R.string.onboarding_code_duplicate_msg)
        binding.tvCodeHint.setTextColor(ContextCompat.getColor(requireContext(), R.color.semantic_red_500))
        binding.tvCodeHintPoint.visibility = View.INVISIBLE
        binding.tvCodeHintEnd.visibility = View.INVISIBLE
    }
}
