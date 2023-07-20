package com.el.yello.presentation.main.profile.manage

import android.content.Intent
import android.os.Bundle
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileQuitBinding
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.yello.presentation.main.profile.manage.ProfileQuitForSureActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileQuitActivity :
    BindingActivity<ActivityProfileQuitBinding>(R.layout.activity_profile_quit) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackButton()
        initReturnButton()
        initProfileQuitForSureActivityWithoutFinish()
    }

    private fun initBackButton() {
        binding.btnProfileQuitBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun initReturnButton() {
        binding.btnProfileQuitReturn.setOnSingleClickListener {
            finish()
        }
    }

    private fun initProfileQuitForSureActivityWithoutFinish() {
        binding.btnProfileQuitResume.setOnSingleClickListener {
            Intent(this, ProfileQuitForSureActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }
}