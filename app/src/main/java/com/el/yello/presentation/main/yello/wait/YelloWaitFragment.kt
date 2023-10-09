package com.el.yello.presentation.main.yello.wait

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.FragmentYelloWaitBinding
import com.el.yello.presentation.main.dialog.InviteFriendDialog
import com.el.yello.presentation.main.yello.YelloViewModel
import com.el.yello.presentation.main.yello.lock.YelloLockFragment.Companion.EVENT_CLICK_INVITE
import com.el.yello.presentation.main.yello.lock.YelloLockFragment.Companion.JSON_INVITE_VIEW
import com.el.yello.presentation.main.yello.lock.YelloLockFragment.Companion.TAG_UNLOCK_DIALOG
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        viewModel.leftTime.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { time ->
                binding.cpbWaitTimer.progress = time.toFloat()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initInviteBtnClickListener() {
        binding.btnWaitInvite.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(
                EVENT_CLICK_INVITE,
                JSONObject().put(JSON_INVITE_VIEW, VALUE_VOTE_40MIN_SCREEN),
            )
            InviteFriendDialog.newInstance(viewModel.getYelloId(), VALUE_VOTE_40MIN_SCREEN)
                .show(parentFragmentManager, TAG_UNLOCK_DIALOG)
        }
    }

    companion object {
        const val VALUE_VOTE_40MIN_SCREEN = "vote_40min_reset"

        @JvmStatic
        fun newInstance() = YelloWaitFragment()
    }
}
