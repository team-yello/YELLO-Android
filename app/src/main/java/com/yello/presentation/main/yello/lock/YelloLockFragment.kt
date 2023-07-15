package com.yello.presentation.main.yello.lock

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentYelloLockBinding
import com.yello.presentation.main.yello.dialog.UnlockDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YelloLockFragment : BindingFragment<FragmentYelloLockBinding>(R.layout.fragment_yello_lock) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInviteBtnClickListener()
    }

    private fun setInviteBtnClickListener() {
        binding.btnLockVote.setOnSingleClickListener {
            UnlockDialogFragment().show(parentFragmentManager, TAG_UNLOCK_DIALOG)
        }
    }

    companion object {
        const val TAG_UNLOCK_DIALOG = "UNLOCK_DIALOG"

        @JvmStatic
        fun newInstance() = YelloLockFragment()
    }
}
