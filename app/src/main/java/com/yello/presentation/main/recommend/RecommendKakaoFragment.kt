package com.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.RecommendModel
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentRecommendKakaoBinding

class RecommendKakaoFragment :
    BindingFragment<FragmentRecommendKakaoBinding>(R.layout.fragment_recommend_kakao) {

    private val viewModel by viewModels<RecommendKakaoViewModel>()
    private lateinit var friendsList : List<RecommendModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addListFromLocal()
        friendsList = viewModel.recommendResult.value ?: emptyList()

        val adapter = RecommendAdapter(requireContext())
        binding.rvRecommendKakao.adapter = adapter
        adapter.setItemList(friendsList)

        binding.layoutInviteFriend.setOnSingleClickListener {

        }

        binding.rvRecommendKakao.itemAnimator = object : DefaultItemAnimator() {
            override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
                holder.itemView.animation =
                    AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_out_right)
                return super.animateRemove(holder)
            }
        }
    }
}