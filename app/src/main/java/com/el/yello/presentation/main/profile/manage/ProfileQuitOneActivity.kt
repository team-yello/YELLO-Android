package com.el.yello.presentation.main.profile.manage

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileQuitOneBinding
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileQuitOneActivity :
    BindingActivity<ActivityProfileQuitOneBinding>(R.layout.activity_profile_quit_one) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackBtnListener()
        initReturnBtnListener()
        initQuitForSureBtnListener()
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

    private fun initQuitForSureBtnListener() {
        binding.btnProfileQuitResume.setOnSingleClickListener {
            Intent(this, ProfileQuitTwoActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }
}
