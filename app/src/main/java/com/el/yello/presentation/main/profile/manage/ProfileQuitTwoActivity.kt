package com.el.yello.presentation.main.profile.manage

import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileQuitTwoBinding
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileQuitTwoActivity :
    BindingActivity<ActivityProfileQuitTwoBinding>(R.layout.activity_profile_quit_two) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackBtnListener()
        initInviteDialogBtnListener()
    }

    private fun initBackBtnListener() {
        binding.btnProfileQuitBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun initInviteDialogBtnListener() {
        binding.btnProfileQuitForSure.setOnSingleClickListener {
            ProfileQuitDialog().show(supportFragmentManager, QUIT_DIALOG)
        }
    }

    private companion object {
        const val QUIT_DIALOG = "quitDialog"
    }
}
