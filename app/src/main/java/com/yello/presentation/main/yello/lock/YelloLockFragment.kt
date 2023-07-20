package com.yello.presentation.main.yello.lock

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentYelloLockBinding
import com.yello.presentation.main.yello.YelloViewModel
import com.yello.presentation.main.yello.dialog.UnlockDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YelloLockFragment : BindingFragment<FragmentYelloLockBinding>(R.layout.fragment_yello_lock) {
    private val viewModel by viewModels<YelloViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInviteBtnClickListener()
    }

    private fun initInviteBtnClickListener() {
        binding.btnLockVote.setOnSingleClickListener {
            UnlockDialogFragment.newInstance(viewModel.getYelloId()).show(parentFragmentManager, TAG_UNLOCK_DIALOG)
        }
    }

    companion object {
        const val TAG_UNLOCK_DIALOG = "UNLOCK_DIALOG"

        @JvmStatic
        fun newInstance() = YelloLockFragment()
    }
}
