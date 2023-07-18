package com.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.yello.R
import com.yello.databinding.FragmentRecommendBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendFragment : BindingFragment<FragmentRecommendBinding>(R.layout.fragment_recommend) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpRecommend.adapter = RecommendViewPagerAdapter(this)
        setTabLayout()
    }

    private fun setTabLayout() {
        val tabTextList = listOf(
            getString(R.string.tv_recommend_tab_kakao),
            getString(R.string.tv_recommend_tab_school)
        )

        TabLayoutMediator(binding.tabRecommend, binding.vpRecommend) { tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()
    }
}
