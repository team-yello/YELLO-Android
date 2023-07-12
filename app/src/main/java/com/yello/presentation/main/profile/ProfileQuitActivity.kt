package com.yello.presentation.main.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ActivityProfileQuitBinding

class ProfileQuitActivity :
    BindingActivity<ActivityProfileQuitBinding>(R.layout.activity_profile_quit) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackButton(this)
        initReturnButton(this)
        initProfileQuitForSureActivityWithoutFinish()
    }

    private fun initBackButton(activity: Activity) {
        binding.btnProfileQuitBack.setOnSingleClickListener {
            activity.finish()
        }
    }

    private fun initReturnButton(activity: Activity) {
        binding.btnProfileQuitReturn.setOnSingleClickListener {
            activity.finish()
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