package com.yello.presentation.main.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileQuitForSureBinding

class ProfileQuitForSureFragment :
    BindingFragment<FragmentProfileQuitForSureBinding>(R.layout.fragment_profile_quit_for_sure) {

    private var profileQuitDialog: ProfileQuitDialog = ProfileQuitDialog()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTransactionButton(binding.btnProfileQuitForSureBack, ProfileManageFragment())
        initInviteDialogButtonListener()
    }

    private fun initTransactionButton(view: View, fragment: Fragment) {
        view.setOnSingleClickListener {
            parentFragmentManager.beginTransaction().apply {
                setReorderingAllowed(true)
                replace(R.id.fcv_main, fragment)
            }.commit()
        }
    }

    private fun initInviteDialogButtonListener() {
        binding.btnProfileQuitForSure.setOnSingleClickListener {
            profileQuitDialog.show(parentFragmentManager, "Dialog")
        }
    }
}