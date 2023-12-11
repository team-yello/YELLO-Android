package com.el.yello.presentation.onboarding.fragment.checkName

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentDialogCheckNameBinding
import com.el.yello.presentation.onboarding.activity.EditNameActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.example.ui.base.BindingDialogFragment
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckNameDialog :
    BindingDialogFragment<FragmentDialogCheckNameBinding>(R.layout.fragment_dialog_check_name) {

    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onStart() {
        super.onStart()
        setDialogBackground()
    }

    private fun setDialogBackground() {
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
        val dialogHorizontalMargin = (Resources.getSystem().displayMetrics.density * 16) * 2

        dialog?.window?.apply {
            setLayout(
                (deviceWidth - dialogHorizontalMargin * 2).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.color.transparent)
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditBtnListener()
    }

    private fun initEditBtnListener() {
        val bundle = arguments
        if (bundle != null) {
            val userKakaoId = bundle.getLong("EXTRA_KAKAO_ID", 0)
            val userName = bundle.getString("EXTRA_NAME", "")
            val userGender = bundle.getString("EXTRA_GENDER", "")
            val userEmail = bundle.getString("EXTRA_EMAIL", "")
            val userImage = bundle.getString("EXTRA_PROFILE_IMAGE", "")

            // 다이얼로그에 카카오 계정 이름 띄우기
            binding.tvNameEditDialogTitleTwo.text = userName

            // 이름 유지
            binding.btnNameEditDialogYes.setOnSingleClickListener {
                val bundle = Bundle().apply {
                    putLong("EXTRA_KAKAO_ID", userKakaoId)
                    putString("EXTRA_NAME", userName)
                    putString("EXTRA_GENDER", userGender)
                    putString("EXTRA_EMAIL", userEmail)
                    putString("EXTRA_PROFILE_IMAGE", userImage)
                }
                val intent = Intent(requireContext(), OnBoardingActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            // 이름 수정
            binding.btnNameEditDialogNo.setOnSingleClickListener {
                val bundle = Bundle().apply {
                    putLong("EXTRA_KAKAO_ID", userKakaoId)
                    putString("EXTRA_NAME", userName)
                    putString("EXTRA_GENDER", userGender)
                    putString("EXTRA_EMAIL", userEmail)
                    putString("EXTRA_PROFILE_IMAGE", userImage)
                }
                val intent = Intent(requireContext(), EditNameActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }
}
