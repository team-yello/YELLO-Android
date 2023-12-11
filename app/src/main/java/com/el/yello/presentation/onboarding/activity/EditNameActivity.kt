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
            val bundle = Bundle().apply {
                putLong("EXTRA_KAKAO_ID", viewModel.kakaoId.toLong())
                putString("EXTRA_NAME", viewModel.nameText.value.toString())
                putString("EXTRA_GENDER", viewModel.gender)
                putString("EXTRA_EMAIL", viewModel.email)
                putString("EXTRA_PROFILE_IMAGE", viewModel.profileImg)
            }

            val intent = Intent(this, OnBoardingActivity::class.java).apply {
                putExtras(bundle)
            }
            Log.d("pmj5", viewModel.kakaoId)
            Log.d("pmj5", viewModel.nameText.value.toString())
            Log.d("pmj5", viewModel.gender)
            Log.d("pmj5", viewModel.email)
            Log.d("pmj5", viewModel.profileImg)
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
            Log.d("pmj4", viewModel.kakaoId)
            Log.d("pmj4", viewModel.nameText.value.toString())
            Log.d("pmj4", viewModel.gender)
            Log.d("pmj4", viewModel.email)
            Log.d("pmj4", viewModel.profileImg)
        }
        // 받아온 이름 값이 비었거나 null 이면 hint 보이도록
        if (viewModel.nameText.value.isNullOrEmpty() || viewModel.nameText.value.isNullOrBlank()) {
            binding.etName.text.clear()
        }
    }

    companion object {
        private const val BACK_PRESSED_INTERVAL = 2000
    }
}
