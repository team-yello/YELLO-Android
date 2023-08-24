package com.el.yello.presentation.onboarding.fragment.code

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.databinding.FragmentCodeBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import org.json.JSONObject

class CodeFragment : BindingFragment<FragmentCodeBinding>(R.layout.fragment_code) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setConfirmBtnCLickListener()
        setDeleteCodeBtnClickListener()
        setupPostSignupState()
        viewModel.validYellIdLoading()
    }

    override fun onResume() {
        super.onResume()
        setupGetValidYelloIdState()
        (activity as? OnBoardingActivity)?.hideBackbtn()
    }

    private fun setConfirmBtnCLickListener() {
        binding.btnCodeSkip.setOnClickListener {
            AmplitudeUtils.trackEventWithProperties("complete_onboarding_finish")
            AmplitudeUtils.trackEventWithProperties(
                "click_onboarding_recommend",
                JSONObject().put("rec_exist", "pass"),
            )
            AmplitudeUtils.updateUserProperties("user_recommend", "no")
            viewModel.postSignup()
        }
        binding.btnCodeNext.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("complete_onboarding_finish")
            AmplitudeUtils.trackEventWithProperties(
                "click_onboarding_recommend",
                JSONObject().put("rec_exist", "next"),
            )
            AmplitudeUtils.updateUserProperties("user_recommend", "yes")
            viewModel.getValidYelloId(viewModel.codeText.value.toString())
            viewModel.postSignup()
        }
    }

    private fun setupPostSignupState() {
        viewModel.postSignupState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    AmplitudeUtils.setUserDataProperties("user_signup_date")
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
        binding.tvCodeWarning.text = getString(R.string.onboarding_code_duplicate_msg)
        binding.tvCodeWarning.setTextColor(Color.parseColor("#F04646"))
    }
}
