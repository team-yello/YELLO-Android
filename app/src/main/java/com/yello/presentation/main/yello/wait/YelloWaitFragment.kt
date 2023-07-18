package com.yello.presentation.main.yello.wait

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentYelloWaitBinding
import com.yello.presentation.main.yello.YelloViewModel
import com.yello.presentation.main.yello.dialog.UnlockDialogFragment
import com.yello.presentation.main.yello.lock.YelloLockFragment.Companion.TAG_UNLOCK_DIALOG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YelloWaitFragment : BindingFragment<FragmentYelloWaitBinding>(R.layout.fragment_yello_wait) {
    val viewModel by activityViewModels<YelloViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initCircularProgressBar()
        initInviteBtnClickListener()
    }

    private fun initCircularProgressBar() {
        binding.cpbWaitTimer.progress = viewModel.leftTime.value?.toFloat() ?: 0f
        viewModel._leftTime.observe(viewLifecycleOwner) { time ->
            binding.cpbWaitTimer.progress = time.toFloat()
        }
    }

    private fun initInviteBtnClickListener() {
        binding.btnWaitInvite.setOnSingleClickListener {
            UnlockDialogFragment().show(parentFragmentManager, TAG_UNLOCK_DIALOG)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = YelloWaitFragment()
    }
}
