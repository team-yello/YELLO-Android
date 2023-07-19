package com.yello.presentation.main.profile.manage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.ui.base.BindingActivity
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.user.UserApiClient
import com.yello.R
import com.yello.databinding.ActivityProfileManageBinding
import timber.log.Timber

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
            logoutKakaoAccount()
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

    // 카카오 로그아웃 후 앱 재시작
    private fun logoutKakaoAccount() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Timber.d(getString(R.string.profile_error_logout))
            } else {
                restartApp(this)
            }
        }
    }

    // 앱 재시작 로직
    private fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}