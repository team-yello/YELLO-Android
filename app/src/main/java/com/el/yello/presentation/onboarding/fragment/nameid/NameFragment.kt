package com.el.yello.presentation.onboarding.fragment.nameid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.el.yello.R
import com.el.yello.databinding.FragmentNameBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import java.util.Timer
import kotlin.concurrent.timer

class NameFragment : BindingFragment<FragmentNameBinding>(R.layout.fragment_name) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    var timer: Timer? = null
    var deltaTime = 32
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setDeleteBtnClickListener()
        setConfirmBtnClickListener()
        progressBarTimerFun()
    }
    private fun progressBarTimerFun() {
        binding.nameProgressbar.progress = 32
        timer?.cancel()
        timer = Timer()
        timer = timer(period = 8, initialDelay = 300) {
            if (deltaTime > 48) cancel()
            binding.nameProgressbar.setProgress(++deltaTime)
            println(binding.nameProgressbar.progress)
        }
    }
    private fun setConfirmBtnClickListener() {
        binding.btnNameNext.setOnSingleClickListener {
            findNavController().navigate(R.id.action_nameFragment_to_yelIoIdFragment)
        }
        binding.btnNameBackBtn.setOnSingleClickListener {
            findNavController().navigate(R.id.action_nameFragment_to_genderFragment)
        }
    }

    private fun setDeleteBtnClickListener() {
        binding.btnNameDelete.setOnSingleClickListener {
            binding.etName.text.clear()
        }
    }
}
