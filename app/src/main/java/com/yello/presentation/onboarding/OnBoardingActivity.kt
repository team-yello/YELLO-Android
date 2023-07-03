package com.yello.presentation.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.yello.R

class OnBoardingActivity : AppCompatActivity() {

    private val data = mutableListOf<String>()
    private val fragmentList = ArrayList<Fragment>()
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        castView()
        addToList()
        fragmentList.add(SchoolFragment())
        fragmentList.add(StudentIdFragment())
        fragmentList.add(NameIdFragment())
        fragmentList.add(GenderFragment())
        fragmentList.add(AddFriendFragment())
        fragmentList.add(CodeFragment())
        fragmentList.add(StartAppFragment())

        viewPager.adapter = ViewPagerFragmentAdapter(this, fragmentList)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun castView() {
        viewPager = findViewById(R.id.view_pager2)
    }

    private fun addToList() {
        for (item in 1..7) {
            data.add("item $item")
        }
    }
}
