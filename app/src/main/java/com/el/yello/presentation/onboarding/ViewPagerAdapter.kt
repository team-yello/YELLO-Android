package com.el.yello.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.el.yello.presentation.onboarding.fragment.addfriend.AddFriendFragment
import com.el.yello.presentation.onboarding.fragment.code.CodeFragment
import com.el.yello.presentation.onboarding.fragment.gender.GenderFragment
import com.el.yello.presentation.onboarding.fragment.gender.SelectStudentFragment
import com.el.yello.presentation.onboarding.fragment.nameid.NameIdFragment
import com.el.yello.presentation.onboarding.fragment.startapp.StartAppFragment
import com.el.yello.presentation.onboarding.fragment.universityinfo.UniversityInfoFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf<Fragment>(
        SelectStudentFragment(),
        UniversityInfoFragment(),
        GenderFragment(),
        NameIdFragment(),
        AddFriendFragment(),
        CodeFragment(),
        StartAppFragment(),

        )

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}
