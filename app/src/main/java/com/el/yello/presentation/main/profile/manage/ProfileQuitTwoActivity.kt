package com.el.yello.presentation.main.profile.manage

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileQuitTwoBinding
import com.el.yello.presentation.main.profile.quit.QuitReasonActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ProfileQuitTwoActivity :
    BindingActivity<ActivityProfileQuitTwoBinding>(R.layout.activity_profile_quit_two) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackBtnListener()
        initQuitBtnListener()
    }

    private fun initBackBtnListener() {
        binding.btnProfileQuitBack.setOnSingleClickListener { finish() }
    }

    private fun initQuitBtnListener() {
        binding.btnProfileQuitForSure.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(
                "click_profile_withdrawal",
                JSONObject().put("withdrawal_button", "withdrawal3"),
            )
            Intent(this, QuitReasonActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }
}
