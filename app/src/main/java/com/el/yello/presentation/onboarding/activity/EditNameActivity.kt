package com.el.yello.presentation.onboarding.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityNameEditBinding
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_EMAIL
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_GENDER
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_KAKAO_ID
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_NAME
import com.el.yello.presentation.auth.SignInActivity.Companion.EXTRA_PROFILE_IMAGE
import com.el.yello.presentation.onboarding.OnBoardingViewModel
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNameActivity :
    BindingActivity<ActivityNameEditBinding>(R.layout.activity_name_edit) {

    private val viewModel by viewModels<OnBoardingViewModel>()

    private var backPressedTime: Long = 0

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime >= BACK_PRESSED_INTERVAL) {
                backPressedTime = System.currentTimeMillis()
                yelloSnackbar(binding.root, getString(R.string.main_toast_back_pressed))
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        setConfirmBtnClickListener()
        setDeleteBtnClickListener()
        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun setConfirmBtnClickListener() {
        getIntentExtraData()
        binding.btnNameNext.setOnSingleClickListener {
            val bundle = Bundle().apply {
                putLong(EXTRA_KAKAO_ID, viewModel.kakaoId.toLong())
                putString(EXTRA_NAME, viewModel.nameText.value.toString())
                putString(EXTRA_GENDER, viewModel.gender)
                putString(EXTRA_EMAIL, viewModel.email)
                putString(EXTRA_PROFILE_IMAGE, viewModel.profileImg)
            }
            Intent(this, OnBoardingActivity::class.java).apply {
                putExtras(bundle)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
            finish()
        }
    }

    private fun setDeleteBtnClickListener() {
        binding.btnNameDelete.setOnSingleClickListener {
            binding.etName.text.clear()
        }
    }

    private fun getIntentExtraData() {
        val bundle: Bundle? = intent.extras
        intent.apply {
            if (bundle != null) {
                with(viewModel) {
                    kakaoId = bundle.getLong(EXTRA_KAKAO_ID, 0).toString()
                    nameText.value = bundle.getString(EXTRA_NAME, "")
                    gender = bundle.getString(EXTRA_GENDER, "")
                    email = bundle.getString(EXTRA_EMAIL, "")
                    profileImg = bundle.getString(EXTRA_PROFILE_IMAGE, "")
                }
            }
        }
        if (viewModel.nameText.value.isNullOrEmpty() || viewModel.nameText.value.isNullOrBlank()) {
            binding.etName.text.clear()
        }
    }

    companion object {
        private const val BACK_PRESSED_INTERVAL = 2000
    }
}
