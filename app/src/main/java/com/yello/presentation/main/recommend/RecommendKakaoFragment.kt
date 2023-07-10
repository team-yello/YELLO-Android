package com.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentRecommendKakaoBinding

class RecommendKakaoFragment : BindingFragment<FragmentRecommendKakaoBinding>(R.layout.fragment_recommend_kakao) {

    private val viewModel by viewModels<RecommendKakaoViewModel>()
    private val recommendAdapter = RecommendAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRecommendKakao.adapter = recommendAdapter

        viewModel.addListFromLocal()
        recommendAdapter.submitList(viewModel.recommendResult.value)

        binding.layoutInviteFriend.setOnSingleClickListener {

        }
    }
}