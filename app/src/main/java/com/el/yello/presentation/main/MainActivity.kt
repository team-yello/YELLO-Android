package com.el.yello.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.el.yello.R
import com.el.yello.databinding.ActivityMainBinding
import com.el.yello.presentation.main.look.LookFragment
import com.el.yello.presentation.main.myyello.MyYelloFragment
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.presentation.main.profile.info.ProfileFragment
import com.el.yello.presentation.main.recommend.RecommendFragment
import com.el.yello.presentation.main.yello.YelloFragment
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    val viewModel by viewModels<ProfileViewModel>()
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBnvItemIconTintList()
        initBnvItemSelectedListener()
        initBnvItemReselectedListener()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime >= 2000) {
            backPressedTime = System.currentTimeMillis()
            toast(getString(R.string.main_toast_back_pressed))
        } else {
            finish()
        }
    }

    private fun initBnvItemIconTintList() {
        binding.bnvMain.itemIconTintList = null
        binding.bnvMain.selectedItemId = R.id.menu_yello
    }

    private fun initBnvItemSelectedListener() {
        supportFragmentManager.findFragmentById(R.id.fcv_main) ?: navigateTo<YelloFragment>()

        binding.bnvMain.setOnItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_recommend -> navigateTo<RecommendFragment>()
                R.id.menu_look -> navigateTo<LookFragment>()
                R.id.menu_yello -> navigateTo<YelloFragment>()
                R.id.menu_my_yello -> navigateTo<MyYelloFragment>()
                R.id.menu_profile -> navigateTo<ProfileFragment>()
            }
            true
        }
    }

    private fun initBnvItemReselectedListener() {
        binding.bnvMain.setOnItemReselectedListener { menu ->
            val currentFragment = supportFragmentManager.fragments[0]
            when (menu.itemId) {
                R.id.menu_recommend -> {
                    if (currentFragment is RecommendFragment) {
                        currentFragment.scrollToTop()
                    }
                }

                R.id.menu_look -> {
                    if (currentFragment is LookFragment) {
                        currentFragment.scrollToTop()
                    }
                }

                R.id.menu_my_yello -> {
                    if (currentFragment is MyYelloFragment) {
                        currentFragment.scrollToTop()
                    }
                }

                R.id.menu_profile -> {
                    if (currentFragment is ProfileFragment) {
                        currentFragment.scrollToTop()
                    }
                }
            }
        }
    }

    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.fcv_main, T::class.java.canonicalName)
        }
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
