package com.el.yello.presentation.onboarding.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityNameEditBinding
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
                toast(getString(R.string.main_toast_back_pressed))
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
            val intent = Intent(this, OnBoardingActivity::class.java).apply {
                intent.putExtra("EXTRA_KAKAO_ID", viewModel.kakaoId.toLong())
                intent.putExtra("EXTRA_NAME", viewModel.nameText.value.toString())
                intent.putExtra("EXTRA_GENDER", viewModel.gender)
                intent.putExtra("EXTRA_EMAIL", viewModel.email)
                intent.putExtra("EXTRA_PROFILE_IMAGE", viewModel.profileImg)
            }
            startActivity(intent)
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
                viewModel.kakaoId = bundle.getLong("EXTRA_KAKAO_ID", 0).toString()
                viewModel.nameText.value = bundle.getString("EXTRA_NAME", "")
                viewModel.gender = bundle.getString("EXTRA_GENDER", "")
                viewModel.email = bundle.getString("EXTRA_EMAIL", "")
                viewModel.profileImg = bundle.getString("EXTRA_PROFILE_IMAGE", "")
            }
        }
    }

    companion object {
        private const val BACK_PRESSED_INTERVAL = 2000
    }
}
