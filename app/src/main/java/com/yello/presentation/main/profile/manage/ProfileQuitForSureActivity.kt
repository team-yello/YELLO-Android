package com.yello.presentation.main.profile.manage

import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ActivityProfileQuitForSureBinding

class ProfileQuitForSureActivity :
    BindingActivity<ActivityProfileQuitForSureBinding>(R.layout.activity_profile_quit_for_sure) {

    private var profileQuitDialog: ProfileQuitDialog = ProfileQuitDialog()

    // TODO: accessToken 받아오기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initReturnButton()
        initInviteDialogButtonListener()
    }

    private fun initReturnButton() {
        binding.btnProfileQuitForSureBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun initInviteDialogButtonListener() {
        binding.btnProfileQuitForSure.setOnSingleClickListener {
            profileQuitDialog.show(supportFragmentManager, "Dialog")
        }
    }
}