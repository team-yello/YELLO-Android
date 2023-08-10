package com.el.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import com.el.yello.R
import com.el.yello.databinding.FragmentRecommendBinding
import com.el.yello.presentation.main.recommend.kakao.RecommendKakaoFragment
import com.el.yello.presentation.main.recommend.school.RecommendSchoolFragment
import com.example.ui.base.BindingFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendFragment : BindingFragment<FragmentRecommendBinding>(R.layout.fragment_recommend) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpRecommend.adapter = RecommendViewPagerAdapter(this)
        setTabLayout()
    }

    private fun setTabLayout() {
        val tabTextList = listOf(TAB_KAKAO, TAB_SCHOOL)

        TabLayoutMediator(binding.tabRecommend, binding.vpRecommend) { tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()
    }

    fun scrollToTop() {
        val currentFragment = childFragmentManager.fragments[binding.vpRecommend.currentItem]
        if (currentFragment is RecommendKakaoFragment) {
            currentFragment.scrollToTop()
        } else if (currentFragment is RecommendSchoolFragment) {
            currentFragment.scrollToTop()
        }
    }

    private companion object {
        const val TAB_KAKAO = "카톡 친구들"
        const val TAB_SCHOOL = "학교 친구들"
    }
}
