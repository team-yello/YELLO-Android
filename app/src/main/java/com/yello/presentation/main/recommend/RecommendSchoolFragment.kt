package com.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentRecommendSchoolBinding

class RecommendSchoolFragment : BindingFragment<FragmentRecommendSchoolBinding>(R.layout.fragment_recommend_school) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutInviteFriend.setOnSingleClickListener {

        }
    }
}