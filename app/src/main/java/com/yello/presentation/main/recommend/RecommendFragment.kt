package com.yello.presentation.main.recommend

import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentRecommendBinding

class RecommendFragment : BindingFragment<FragmentRecommendBinding>(R.layout.fragment_recommend) {

    companion object {
        @JvmStatic
        fun newInstance() = RecommendFragment()
    }
}
