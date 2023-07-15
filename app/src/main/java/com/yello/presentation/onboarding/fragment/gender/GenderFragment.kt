package com.yello.presentation.onboarding.fragment.gender

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentGenderBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel

class GenderFragment : BindingFragment<FragmentGenderBinding>(R.layout.fragment_gender) {

    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setConfirmBtnClickListener()
        setBackBtnClickListener()
        clickBoyBtnClickListener()
        clickGirlBtnClickListener()
        // binding.vm=viewModel
    }

    private fun setConfirmBtnClickListener() {
        binding.btnGenderNext.setOnSingleClickListener {
            viewModel.navigateToNextPage()
        }
    }

    private fun setBackBtnClickListener() {
        binding.btnGenderBackBtn.setOnSingleClickListener {
            viewModel.navigateToBackPage()
        }
    }

    private fun clickBoyBtnClickListener() {
        binding.btnGenderX.setOnClickListener {
            binding.btnGenderX.setBackgroundResource(R.drawable.shape_onboarding_semantic_gender_m700_fill_semantic_gender_m300_line)
            binding.ivOnboardingYelloFaceX.setImageResource(R.drawable.ic_onboarding_yello_face_white)
            binding.tvBoy.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.ivBoyUncheck.setImageResource(R.drawable.ic_onboarding_check_circle_boy)

            binding.btnGenderY.setBackgroundResource(R.drawable.shape_grayscales_800_fill_8_rect)
            binding.ivOnboardingYelloFaceY.setImageResource(R.drawable.ic_onboarding_yello_face_grey)
            binding.tvGirl.setTextColor(Color.parseColor("#495057"))
            binding.ivGirlUncheck.setImageResource(R.drawable.ic_onbarding_check_circle_grey)
        }
    }

    private fun clickGirlBtnClickListener() {
        binding.btnGenderY.setOnClickListener {
            binding.btnGenderY.setBackgroundResource(R.drawable.shape_genderf_700_fill_genderf_300_line_rect_8)
            binding.ivOnboardingYelloFaceY.setImageResource(R.drawable.ic_onboarding_yello_face_white)
            binding.tvGirl.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.ivGirlUncheck.setImageResource(R.drawable.ic_onboarding_check_circle_girl)

            binding.btnGenderX.setBackgroundResource(R.drawable.shape_grayscales_800_fill_8_rect)
            binding.ivOnboardingYelloFaceX.setImageResource(R.drawable.ic_onboarding_yello_face_grey)
            binding.tvBoy.setTextColor(Color.parseColor("#495057"))
            binding.ivBoyUncheck.setImageResource(R.drawable.ic_onbarding_check_circle_grey)
        }
    }
}
