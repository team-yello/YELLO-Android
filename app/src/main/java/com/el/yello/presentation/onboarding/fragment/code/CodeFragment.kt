package com.el.yello.presentation.onboarding.fragment.code

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentCodeBinding
import com.el.yello.presentation.onboarding.activity.GetAlarmActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.onboarding.OnBoardingViewModel
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.fragment.colorOf
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import org.json.JSONObject

class CodeFragment : BindingFragment<FragmentCodeBinding>(R.layout.fragment_code) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        observePostSignupState()
        observeGetValidYelloIdState()
        setCodeBtnCLickListener()
        setDeleteCodeBtnClickListener()
    }

    override fun onResume() {
        super.onResume()
        (activity as? OnBoardingActivity)?.hideBackBtn()
    }

    private fun setCodeBtnCLickListener() {
        binding.btnCodeSkip.setOnClickListener {
            viewModel.postSignup()
            amplitudeCodeSkipInfo()
        }
        binding.btnCodeNext.setOnSingleClickListener {
            viewModel.getValidYelloId(viewModel.codeText.value.toString())
            amplitudeCodeNextInfo()
        }
    }

    private fun observeGetValidYelloIdState() {
        viewModel.getValidYelloIdState.observe(viewLifecycleOwner) { state ->
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

    private fun observePostSignupState() {
        viewModel.postSignupState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    AmplitudeUtils.setUserDataProperties(PROPERTY_USER_SIGHUP_DATE)
                    val intent = Intent(activity, GetAlarmActivity::class.java)
                    startActivity(intent)
                    (activity as? OnBoardingActivity)?.endTutorialActivity()
                }
                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }
                is UiState.Loading -> {}
                is UiState.Empty -> {}
            }
        }
    }

    private fun setDeleteCodeBtnClickListener() {
        binding.ivCodeDelete.setOnClickListener {
            binding.etCode.text.clear()
        }
    }

    private fun initIdEditTextViewError() {
        with(binding) {
            etCode.setBackgroundResource(R.drawable.shape_fill_red20_line_semantic_status_red500_rect_8)
            ivCodeDelete.setBackgroundResource(R.drawable.ic_onboarding_delete_red)
            tvCodeWarning.text = getString(R.string.onboarding_code_duplicate_msg)
            tvCodeWarning.setTextColor(colorOf(R.color.semantic_red_500))
        }
    }

    private fun amplitudeCodeSkipInfo() {
        with(AmplitudeUtils) {
            trackEventWithProperties(EVENT_COMPLETE_ONBOARDING_FINISH)
            trackEventWithProperties(
                EVENT_CLICK_ONBOARDING_RECOMMEND,
                JSONObject().put(NAME_REC_EXIST, VALUE_PASS),
            )
            updateUserProperties(PROPERTY_USER_RECOMMEND, VALUE_NO)
            updateUserProperties(
                PROPERTY_USER_NAME,
                viewModel.nameText.value.toString(),
            )
            updateUserProperties(PROPERTY_USER_SEX, viewModel.gender)
        }
    }

    private fun amplitudeCodeNextInfo() {
        with(AmplitudeUtils) {
            trackEventWithProperties(EVENT_COMPLETE_ONBOARDING_FINISH)
            trackEventWithProperties(
                EVENT_CLICK_ONBOARDING_RECOMMEND,
                JSONObject().put(NAME_REC_EXIST, VALUE_NEXT),
            )
            updateUserProperties(PROPERTY_USER_RECOMMEND, VALUE_YES)
            updateUserProperties(
                PROPERTY_USER_NAME,
                viewModel.nameText.value.toString(),
            )
            updateUserProperties(PROPERTY_USER_SEX, viewModel.gender)
        }
    }

    companion object {
        private const val PROPERTY_USER_SIGHUP_DATE = "user_signup_date"
        private const val EVENT_COMPLETE_ONBOARDING_FINISH = "complete_onboarding_finish"
        private const val EVENT_CLICK_ONBOARDING_RECOMMEND = "click_onboarding_recommend"
        private const val NAME_REC_EXIST = "rec_exist"
        private const val VALUE_PASS = "pass"
        private const val VALUE_NEXT = "next"
        private const val PROPERTY_USER_RECOMMEND = "user_recommend"
        private const val VALUE_NO = "no"
        private const val VALUE_YES = "yes"
        private const val PROPERTY_USER_NAME = "user_name"
        private const val PROPERTY_USER_SEX = "user_sex"
    }
}
