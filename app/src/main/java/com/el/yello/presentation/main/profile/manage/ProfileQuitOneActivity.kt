package com.el.yello.presentation.main.profile.manage

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileQuitOneBinding
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ProfileQuitOneActivity :
    BindingActivity<ActivityProfileQuitOneBinding>(R.layout.activity_profile_quit_one) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackBtnListener()
        initReturnBtnListener()
        initQuitBtnListener()
    }

    private fun initBackBtnListener() {
        binding.btnProfileQuitForSureBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun initReturnBtnListener() {
        binding.btnProfileQuitReturn.setOnSingleClickListener {
            finish()
        }
    }

    private fun initQuitBtnListener() {
        binding.btnProfileQuitResume.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(
                "click_profile_withdrawal",
                JSONObject().put("withdrawal_button", "withdrawal2")
            )
            Intent(this, ProfileQuitTwoActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }
}
