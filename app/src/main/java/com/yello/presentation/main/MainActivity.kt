package com.yello.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.ui.base.BindingActivity
import com.yello.R
import com.yello.databinding.ActivityMainBinding
import com.yello.presentation.main.look.LookFragment
import com.yello.presentation.main.myyello.MyYelloFragment
import com.yello.presentation.main.profile.ProfileFragment
import com.yello.presentation.main.profile.ProfileViewModel
import com.yello.presentation.main.recommend.RecommendFragment
import com.yello.presentation.main.yello.YelloFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBnvItemIconTintList()
        initBnvItemSelectedListener()
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

    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.fcv_main, T::class.java.canonicalName)
        }
    }
}
