package com.el.yello.presentation.main.recommend

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.el.yello.R
import com.el.yello.databinding.FragmentRecommendBinding
import com.el.yello.presentation.main.recommend.kakao.RecommendKakaoFragment
import com.el.yello.presentation.main.recommend.kakao.RecommendKakaoViewModel
import com.el.yello.presentation.main.recommend.school.RecommendSchoolFragment
import com.el.yello.presentation.main.recommend.school.RecommendSchoolViewModel
import com.el.yello.presentation.search.SearchActivity
import com.el.yello.util.manager.AmplitudeManager
import com.example.ui.base.BindingFragment
import com.example.ui.extension.setOnSingleClickListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendFragment : BindingFragment<FragmentRecommendBinding>(R.layout.fragment_recommend) {

    private lateinit var kakaoViewModel: RecommendKakaoViewModel
    private lateinit var schoolViewModel: RecommendSchoolViewModel

    private val tabTextList = listOf(TAB_KAKAO, TAB_SCHOOL)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModelProvider()
        initSearchBtnListener()
        setTabLayout()
    }

    private fun initViewModelProvider() {
        kakaoViewModel = ViewModelProvider(requireActivity())[RecommendKakaoViewModel::class.java]
        schoolViewModel = ViewModelProvider(requireActivity())[RecommendSchoolViewModel::class.java]
    }

    private fun initSearchBtnListener() {
        binding.btnRecommendSearch.setOnSingleClickListener {
            AmplitudeManager.trackEventWithProperties("click_search_button")
            kakaoViewModel.isSearchViewShowed = true
            schoolViewModel.isSearchViewShowed = true
            Intent(activity, SearchActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }

    private fun setTabLayout() {
        binding.vpRecommend.adapter = RecommendViewPagerAdapter(this)
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
