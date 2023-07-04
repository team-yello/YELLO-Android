package com.yello.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yello.presentation.onboarding.fragment.AddFriendFragment
import com.yello.presentation.onboarding.fragment.CodeFragment
import com.yello.presentation.onboarding.fragment.GenderFragment
import com.yello.presentation.onboarding.fragment.NameIdFragment
import com.yello.presentation.onboarding.fragment.SchoolFragment
import com.yello.presentation.onboarding.fragment.StudentIdFragment

class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private lateinit var viewPagerAdapter: ViewPagerFragmentAdapter
    val fragments = listOf<Fragment>(AddFriendFragment(), CodeFragment(), GenderFragment(), NameIdFragment(), SchoolFragment(), StudentIdFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
