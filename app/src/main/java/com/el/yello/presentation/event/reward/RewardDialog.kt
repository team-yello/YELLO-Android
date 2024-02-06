package com.el.yello.presentation.event.reward

import android.os.Bundle
import android.view.View
import com.el.yello.R
import com.el.yello.databinding.FragmentRewardDialogBinding
import com.example.ui.base.BindingDialogFragment

class RewardDialog :
    BindingDialogFragment<FragmentRewardDialogBinding>(R.layout.fragment_reward_dialog) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RewardDialog()
    }
}
