package com.el.yello.presentation.main.profile.manage

import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileQuitTwoBinding
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ProfileQuitTwoActivity :
    BindingActivity<ActivityProfileQuitTwoBinding>(R.layout.activity_profile_quit_two) {

    private var profileQuitDialog: ProfileQuitDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileQuitDialog = ProfileQuitDialog()
        initBackBtnListener()
        initInviteDialogBtnListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        profileQuitDialog?.dismiss()
    }

    private fun initBackBtnListener() {
        binding.btnProfileQuitBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun initInviteDialogBtnListener() {
        binding.btnProfileQuitForSure.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(
                "click_profile_withdrawal",
                JSONObject().put("withdrawal_button", "withdrawal3")
            )
            profileQuitDialog?.show(supportFragmentManager, QUIT_DIALOG)
        }
    }

    private companion object {
        const val QUIT_DIALOG = "quitDialog"
    }
}