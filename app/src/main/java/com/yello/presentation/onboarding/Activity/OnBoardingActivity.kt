package com.yello.presentation.onboarding.Activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.ui.base.BindingActivity
import com.yello.R
import com.yello.databinding.ActivityOnboardingBinding
import com.yello.presentation.onboarding.fragment.AddFriendFragment
import com.yello.presentation.onboarding.fragment.CodeFragment
import com.yello.presentation.onboarding.fragment.GenderFragment
import com.yello.presentation.onboarding.fragment.NameIdFragment
import com.yello.presentation.onboarding.fragment.SchoolFragment
import com.yello.presentation.onboarding.fragment.StartAppFragment
import com.yello.presentation.onboarding.fragment.StudentIdFragment
import com.yello.presentation.onboarding.ViewPagerFragmentAdapter

class OnBoardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private val data = mutableListOf<String>()
    private val fragmentList = ArrayList<Fragment>()
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addToList()
        fragmentList.add(SchoolFragment())
        fragmentList.add(StudentIdFragment())
        fragmentList.add(NameIdFragment())
        fragmentList.add(GenderFragment())
        fragmentList.add(AddFriendFragment())
        fragmentList.add(CodeFragment())
        fragmentList.add(StartAppFragment())

        //viewPager.adapter = ViewPagerFragmentAdapter(this, fragmentList)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }


    private fun addToList() {
        for (item in 1..7) {
            data.add("item $item")
        }
    }
}
