package com.yello.presentation.onboarding.fragment.startapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentStartappBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel

class StartAppFragment : BindingFragment<FragmentStartappBinding>(R.layout.fragment_startapp) {

    private val viewModel by activityViewModels<OnBoardingViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO 대부분의 로직은 여기에 구현합니다.
    }
}
