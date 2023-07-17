package com.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.RecommendModel
import com.example.ui.base.BindingFragment
import com.example.ui.fragment.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.yello.R
import com.yello.databinding.FragmentRecommendSchoolBinding
import com.yello.presentation.main.profile.info.ProfileFriendAdapter
import com.yello.presentation.main.profile.info.ProfileFriendItemBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RecommendSchoolFragment :
    BindingFragment<FragmentRecommendSchoolBinding>(R.layout.fragment_recommend_school) {

    private val viewModel by viewModels<RecommendSchoolViewModel>()
    private var adapter: RecommendAdapter? = null

    private var recommendInviteDialog: RecommendInviteDialog = RecommendInviteDialog()

    private lateinit var friendsList: List<RecommendModel>
    private lateinit var kakaoFriendIdList: List<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFriendIdList()
        initInviteButtonListener()
        initItemClickListener()
        observeChangeTokenState()
        setListFromServer()
        setItemDivider()
        setDeleteAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissDialog()
    }

    private fun getFriendIdList() {
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Timber.e(error, "카카오 친구목록 가져오기 실패")
            } else if (friends != null) {
                val friendList: List<Friend>? = friends.elements
                kakaoFriendIdList = friendList?.map { friend -> friend.id.toString() } ?: listOf()
            } else {
                Timber.d("연동 가능한 카카오톡 친구 없음")
            }
        }
    }

    private fun initInviteButtonListener() {
        binding.layoutInviteFriend.setOnSingleClickListener {
            recommendInviteDialog.show(parentFragmentManager, "Dialog")
        }
        binding.btnRecommendNoFriend.setOnSingleClickListener {
            recommendInviteDialog.show(parentFragmentManager, "Dialog")
        }
    }

    private fun setListFromServer() {
        viewModel.addListFromServer(1)
    }

    private fun initItemClickListener() {
        adapter = RecommendAdapter{ recommendModel ->

        }
    }

    private fun observeChangeTokenState() {
        viewModel.postState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    binding.layoutRecommendFriendsList.isVisible = true
                    friendsList = state.data
                    binding.rvRecommendSchool.adapter = adapter?.apply {
                        setItemList(friendsList)
                    }
                }

                is UiState.Failure -> {
                    binding.layoutRecommendFriendsList.isVisible = false
                    binding.layoutRecommendNoFriendsList.isVisible = true
                    toast(state.msg)
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {
                    binding.layoutRecommendFriendsList.isVisible = false
                    binding.layoutRecommendNoFriendsList.isVisible = true
                }
            }
        }
    }

    private fun setItemDivider() {
        binding.rvRecommendSchool.addItemDecoration(RecommendItemDecoration(requireContext()))
    }

    private fun setDeleteAnimation() {
        binding.rvRecommendSchool.itemAnimator = object : DefaultItemAnimator() {
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