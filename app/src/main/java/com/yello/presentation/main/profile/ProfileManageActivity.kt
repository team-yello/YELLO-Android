package com.yello.presentation.main.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ActivityProfileManageBinding

class ProfileManageActivity :
    BindingActivity<ActivityProfileManageBinding>(R.layout.activity_profile_manage) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackButton(this)
        initProfileQuitActivityWithoutFinish()


        binding.btnProfileManageCenter.setOnSingleClickListener {
            // TODO: 버튼 3개 링크 설정
        }

        binding.btnProfileManageLogout.setOnSingleClickListener {
            // TODO: 로그아웃 설정
        }
    }

    private fun initBackButton(activity: Activity) {
        binding.btnProfileManageBack.setOnSingleClickListener {
            activity.finish()
        }
    }

    private fun initProfileQuitActivityWithoutFinish() {
        binding.btnProfileManageQuit.setOnSingleClickListener {
            Intent(this, ProfileQuitActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }
}