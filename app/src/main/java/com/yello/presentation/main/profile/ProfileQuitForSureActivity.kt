package com.yello.presentation.main.profile

import android.app.Activity
import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ActivityProfileQuitForSureBinding

class ProfileQuitForSureActivity :
    BindingActivity<ActivityProfileQuitForSureBinding>(R.layout.activity_profile_quit_for_sure) {

    private var profileQuitDialog: ProfileQuitDialog = ProfileQuitDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initReturnButton(this)
        initInviteDialogButtonListener()
    }

    private fun initReturnButton(activity: Activity) {
        binding.btnProfileQuitForSureBack.setOnSingleClickListener {
            activity.finish()
        }
    }

    private fun initInviteDialogButtonListener() {
        binding.btnProfileQuitForSure.setOnSingleClickListener {
            profileQuitDialog.show(supportFragmentManager, "Dialog")
        }
    }
}