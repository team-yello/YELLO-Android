package com.el.yello.presentation.main.yello.lock

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentYelloLockBinding
import com.el.yello.presentation.main.dialog.InviteFriendDialog
import com.el.yello.presentation.main.yello.YelloViewModel
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class YelloLockFragment : BindingFragment<FragmentYelloLockBinding>(R.layout.fragment_yello_lock) {
    private val viewModel by activityViewModels<YelloViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInviteBtnClickListener()
    }

    private fun initInviteBtnClickListener() {
        binding.btnLockVote.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(
                "click_invite",
                JSONObject().put("invite_view", VOTE_4_DOWN),
            )
            InviteFriendDialog.newInstance(viewModel.getYelloId(), VOTE_4_DOWN)
                .show(parentFragmentManager, TAG_UNLOCK_DIALOG)
        }
    }

    companion object {
        const val TAG_UNLOCK_DIALOG = "UNLOCK_DIALOG"
        const val VOTE_4_DOWN = "vote_4down"

        @JvmStatic
        fun newInstance() = YelloLockFragment()
    }
}
