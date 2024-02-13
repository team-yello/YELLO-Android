package com.el.yello.presentation.onboarding.fragment.code

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import com.el.yello.R
import com.el.yello.databinding.FragmentCodeBinding
import com.el.yello.presentation.onboarding.OnBoardingViewModel
import com.el.yello.presentation.onboarding.activity.GetAlarmActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.fragment.colorOf
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class CodeFragment : BindingFragment<FragmentCodeBinding>(R.layout.fragment_code) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()
    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        observeGetValidYelloIdState()
        observeEditTextForValidCode()
        setDeleteCodeBtnClickListener()
        observePostSignupState()
        setConfirmBtnCLickListener()
    }

    override fun onResume() {
        super.onResume()
        callParentActivity {
            hideBackBtn()
        }
    }

    private fun callParentActivity(callback: OnBoardingActivity.() -> Unit) {
        val activity = requireActivity()
        if (activity is OnBoardingActivity) {
            activity.callback()
        }
    }

    private fun setConfirmBtnCLickListener() {
        binding.btnCodeSkip.setOnSingleClickListener {
            amplitudeCodeSkipInfo()
            if (viewModel.getValidYelloIdState.value is UiState.Success && !(viewModel.getValidYelloIdState.value as UiState.Success).data) {
                viewModel.codeText.value = ""
            }
            viewModel.postSignup()
        }
        binding.btnCodeNext.setOnSingleClickListener {
            amplitudeCodeNextInfo()
            viewModel.postSignup()
        }
    }

    private fun observeEditTextForValidCode() {
        binding.etCode.doAfterTextChanged { input ->
            searchJob?.cancel()
            searchJob = viewModel.viewModelScope.launch {
                delay(debounceTime)
                input?.toString()?.let {
                    viewModel.getValidYelloId(it)
                    binding.btnCodeNext.isEnabled = true
                }
            }
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
            btnCodeNext.setBackgroundResource(R.drawable.shape_grayscales_800_fill_100_rect)
            btnCodeNext.setTextColor(colorOf(R.color.grayscales_700))
            btnCodeNext.isEnabled = false
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
        private const val debounceTime = 300L
    }
}
