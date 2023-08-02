package com.el.yello.presentation.onboarding.fragment.school

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentSchoolBinding
import com.el.yello.presentation.auth.SocialSyncActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.presentation.onboarding.fragment.school.dialog.SearchDialogSchoolFragment
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import java.util.Timer
import kotlin.concurrent.timer

class SchoolFragment : BindingFragment<FragmentSchoolBinding>(R.layout.fragment_school) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    var timer: Timer? = null
    var deltaTime = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.progressbar.progress = 20
        initSearchSchoolBtnClickListener()
        setConfirmBtnClickListener()
        setupSchool()

        binding.btn.setOnClickListener { ProgressBarTimerFun() }
    }
    fun ProgressBarTimerFun() {
        deltaTime = 20 // 기존의 20%를 채움
        binding.progressbar.progress = 20
        timer?.cancel()
        timer = Timer()
        timer = timer(period = 10, initialDelay = 1000) {
            if (deltaTime > 40) cancel()
            binding.progressbar.setProgress(++deltaTime)
            println(binding.progressbar.progress)
        }
    }

    private fun initSearchSchoolBtnClickListener() {
        binding.tvSchoolSearch.setOnSingleClickListener {
            SearchDialogSchoolFragment().show(parentFragmentManager, this.tag)
        }
    }

    private fun setConfirmBtnClickListener() {
        binding.btnSchoolNext.setOnSingleClickListener {
            viewModel.navigateToNextPage()
        }
        binding.btnSchoolBackBtn.setOnSingleClickListener {
            val intent = Intent(activity, SocialSyncActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupSchool() {
        viewModel._school.observe(viewLifecycleOwner) { school ->
            binding.tvSchoolSearch.text = school
        }
    }
}
