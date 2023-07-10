package com.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.yello.R
import com.yello.databinding.FragmentRecommendBinding

class RecommendFragment : BindingFragment<FragmentRecommendBinding>(R.layout.fragment_recommend) {

    private val tabTextList = listOf("카톡 친구들", "학교 친구들")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpRecommend.adapter =ViewPagerAdapter(this)

        TabLayoutMediator(binding.tabRecommend, binding.vpRecommend) { tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()
    }
}
