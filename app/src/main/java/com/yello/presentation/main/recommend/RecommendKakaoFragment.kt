package com.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
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
import com.yello.databinding.FragmentRecommendKakaoBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RecommendKakaoFragment :
    BindingFragment<FragmentRecommendKakaoBinding>(R.layout.fragment_recommend_kakao) {

    private val viewModel by viewModels<RecommendKakaoViewModel>()

    private var recommendInviteDialog: RecommendInviteDialog = RecommendInviteDialog()

    private lateinit var friendsList: List<RecommendModel>
    private lateinit var kakaoFriendIdList: List<String>

    // TODO: 액세스 토큰 설정
    private val serviceAccessToken =
        "eyJ0eXBlIjoiYWNjZXNzVG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyOTAyMTQ3MTY5IiwianRpIjoiMTYxIiwiaWF0IjoxNjg5NTMzMjgyLCJleHAiOjE2ODk2MTk2ODJ9.yzO71BRbZLoitkr0iv6R2JYEjp-e2RMUZVQHMm81RDI"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFriendIdList()
        initInviteButtonListener()
        observeChangeTokenState()
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
                setListFromServer()
            } else {
                Timber.d("연동 가능한 카카오톡 친구 없음")
            }
        }
    }

    private fun initInviteButtonListener() {
        binding.layoutInviteFriend.setOnSingleClickListener {
            recommendInviteDialog.show(parentFragmentManager, "Dialog")
        }
    }

    private fun setListFromServer() {
        val token = "Bearer $serviceAccessToken"
        viewModel.addListFromServer(token, 1, kakaoFriendIdList)
    }

    private fun observeChangeTokenState() {
        viewModel.postState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    friendsList = state.data
                    binding.rvRecommendKakao.adapter = RecommendAdapter(requireContext()).apply {
                        setItemList(friendsList)
                    }
                }

                is UiState.Failure -> {
                    toast(state.msg)
                }

                is UiState.Loading -> {}
                is UiState.Empty -> {}
            }
        }
    }

    private fun setItemDivider() {
        binding.rvRecommendKakao.addItemDecoration(RecommendItemDecoration(requireContext()))
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