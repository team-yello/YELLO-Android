package com.el.yello.presentation.main.yello.wait

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentYelloWaitBinding
import com.el.yello.presentation.main.yello.YelloViewModel
import com.el.yello.presentation.main.yello.dialog.UnlockDialogFragment
import com.el.yello.presentation.main.yello.lock.YelloLockFragment.Companion.TAG_UNLOCK_DIALOG
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class YelloWaitFragment : BindingFragment<FragmentYelloWaitBinding>(R.layout.fragment_yello_wait) {
    private val viewModel by activityViewModels<YelloViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initCircularProgressBar()
        initInviteBtnClickListener()
    }

    private fun initCircularProgressBar() {
        binding.cpbWaitTimer.progress = viewModel.leftTime.value?.toFloat() ?: 0f
        viewModel.leftTime.observe(viewLifecycleOwner) { time ->
            binding.cpbWaitTimer.progress = time.toFloat()
        }
    }

    private fun initInviteBtnClickListener() {
        binding.btnWaitInvite.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(
                "click_invite",
                JSONObject().put("invite_view", VOTE_40MIN_SCREEN)
            )
            UnlockDialogFragment.newInstance(viewModel.getYelloId(), VOTE_40MIN_SCREEN)
                .show(parentFragmentManager, TAG_UNLOCK_DIALOG)
        }
    }

    companion object {

        const val VOTE_40MIN_SCREEN = "vote_40min_reset"

        @JvmStatic
        fun newInstance() = YelloWaitFragment()
    }
}
