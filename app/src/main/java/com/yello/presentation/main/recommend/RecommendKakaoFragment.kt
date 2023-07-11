package com.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentRecommendKakaoBinding

class RecommendKakaoFragment :
    BindingFragment<FragmentRecommendKakaoBinding>(R.layout.fragment_recommend_kakao) {

    private val viewModel by viewModels<RecommendKakaoViewModel>()
    private var recommendInviteDialog: RecommendInviteDialog = RecommendInviteDialog()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInviteButtonListener()
        setListToAdapterFromLocal()
        setDeleteAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissDialog()
    }

    private fun initInviteButtonListener() {
        binding.layoutInviteFriend.setOnSingleClickListener {
            recommendInviteDialog.show(parentFragmentManager, "Dialog")
        }
    }

    private fun setListToAdapterFromLocal() {
        viewModel.addListFromLocal()
        val friendsList = viewModel.recommendResult.value ?: emptyList()
        binding.rvRecommendKakao.adapter = RecommendAdapter(requireContext()).apply {
            setItemList(friendsList)
        }
    }

    private fun setDeleteAnimation() {
        binding.rvRecommendKakao.itemAnimator = object : DefaultItemAnimator() {
            override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
                holder.itemView.animation =
                    AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_out_right)
                return super.animateRemove(holder)
            }
        }
    }

    private fun dismissDialog() {
        if (recommendInviteDialog.isAdded) recommendInviteDialog.dismiss()
    }
}