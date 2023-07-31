package com.el.yello.presentation.main.recommend

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.el.yello.presentation.main.recommend.kakao.RecommendKakaoFragment
import com.el.yello.presentation.main.recommend.school.RecommendSchoolFragment

class RecommendViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecommendKakaoFragment()
            else -> RecommendSchoolFragment()
        }
    }
}
