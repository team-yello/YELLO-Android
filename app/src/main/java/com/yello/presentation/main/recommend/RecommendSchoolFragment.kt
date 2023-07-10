package com.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentRecommendSchoolBinding

class RecommendSchoolFragment : BindingFragment<FragmentRecommendSchoolBinding>(R.layout.fragment_recommend_school) {

    private val viewModel by viewModels<RecommendSchoolViewModel>()
    private val recommendAdapter = RecommendAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addListFromLocal()
        recommendAdapter.submitList(viewModel.recommendResult.value)
        binding.rvRecommendSchool.adapter = recommendAdapter

        binding.layoutInviteFriend.setOnSingleClickListener {

        }
    }
}