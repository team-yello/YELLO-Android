package com.el.yello.presentation.onboarding.fragment.highschoolinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentHighschoolBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.example.ui.base.BindingFragment
import java.util.Timer
import kotlin.concurrent.timer

class HighschoolInfoFragment :
    BindingFragment<FragmentHighschoolBinding>(R.layout.fragment_highschool) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    var timer: Timer? = null
    var deltaTime = 16
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        ProgressBarTimerFun()
    }

    private fun ProgressBarTimerFun() {
        binding.highschoolProgressbar.progress = 16
        timer?.cancel()
        timer = Timer()
        timer = timer(period = 8, initialDelay = 300) {
            if (deltaTime > 32) cancel()
            binding.highschoolProgressbar.setProgress(++deltaTime)
            println(binding.highschoolProgressbar.progress)
        }
    }
}
