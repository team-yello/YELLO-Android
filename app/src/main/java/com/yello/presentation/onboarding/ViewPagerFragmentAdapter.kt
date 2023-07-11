package com.yello.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yello.presentation.onboarding.fragment.AddFriendFragment
import com.yello.presentation.onboarding.fragment.CodeFragment
import com.yello.presentation.onboarding.fragment.GenderFragment
import com.yello.presentation.onboarding.fragment.NameIdFragment
import com.yello.presentation.onboarding.fragment.school.SchoolFragment
import com.yello.presentation.onboarding.fragment.StartAppFragment
import com.yello.presentation.onboarding.fragment.studentid.StudentIdFragment

class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf<Fragment>(SchoolFragment(), StudentIdFragment(), NameIdFragment(), GenderFragment(), AddFriendFragment(), CodeFragment(), StartAppFragment())

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}
