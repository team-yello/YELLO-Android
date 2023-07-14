package com.yello.presentation.main.yello.wait

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentYelloWaitBinding
import com.yello.presentation.main.yello.YelloViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YelloWaitFragment : BindingFragment<FragmentYelloWaitBinding>(R.layout.fragment_yello_wait) {
    val viewModel by activityViewModels<YelloViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initCircularProgressBar()
        startTimer()
    }

    private fun initCircularProgressBar() {
        viewModel.leftTime.observe(viewLifecycleOwner) { time ->
            binding.cpbWaitTimer.progress = time.toFloat()
        }
    }

    private fun startTimer() {
        viewModel.decreaseTime()
    }

    companion object {
        @JvmStatic
        fun newInstance() = YelloWaitFragment()
    }
}
