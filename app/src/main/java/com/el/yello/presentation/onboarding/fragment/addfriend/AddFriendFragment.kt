package com.el.yello.presentation.onboarding.fragment.addfriend

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentAddFriendBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.entity.onboarding.AddFriendListModel.FriendModel
import com.example.ui.base.BindingFragment
import com.example.ui.fragment.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class AddFriendFragment : BindingFragment<FragmentAddFriendBinding>(R.layout.fragment_add_friend) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    private var _adapter: AddFriendAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private lateinit var friendsList: List<FriendModel>

    private var selectedItemIdList = mutableListOf<Long>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initFriendAdapter()
        setConfirmBtnClickListener()
        setKakaoRecommendList()
        observeAddListState()
        (activity as? OnBoardingActivity)?.showBackbtn()
    }

    private fun initFriendAdapter() {
        _adapter = AddFriendAdapter { friend, position ->
            friend.isSelected = !friend.isSelected
            if (friend.isSelected && friend.id !in selectedItemIdList) {
                selectedItemIdList.add(friend.id)
                viewModel.selectedFriendCount.value = viewModel.selectedFriendCount.value?.plus(1)
            } else {
                selectedItemIdList.remove(friend.id)
                viewModel.selectedFriendCount.value = viewModel.selectedFriendCount.value?.minus(1)
            }
            adapter.notifyItemChanged(position)
        }
        binding.rvFriendList.adapter = adapter
    }

    private fun setConfirmBtnClickListener() {
        binding.btnAddFriendNext.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties(
                "click_onboarding_next",
                JSONObject().put("onboard_view", "friends"),
            )
            val activity = requireActivity() as OnBoardingActivity
            activity.progressBarPlus()
            viewModel.selectedFriendIdList = selectedItemIdList
            findNavController().navigate(R.id.action_addFriendFragment_to_codeFragment)
        }
    }

    // 서버 통신 성공 시 카카오 추천 친구 추가
    private fun setKakaoRecommendList() {
        adapter.submitList(listOf())
        setListWithInfinityScroll()
        viewModel.initFriendPagingVariable()
        viewModel.addListWithKakaoIdList()
    }

    // 무한 스크롤 구현
    private fun setListWithInfinityScroll() {
        binding.rvFriendList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvFriendList.canScrollVertically(1) &&
                            layoutManager is LinearLayoutManager &&
                            layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1
                        ) {
                            viewModel.addListWithKakaoIdList()
                        }
                    }
                }
            }
        })
    }

    // 리스트 추가 서버 통신 성공 시 어댑터에 리스트 추가
    private fun observeAddListState() {
        viewModel.friendListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    stopShimmerView()
                    friendsList = state.data.friendList
                    adapter.submitList(friendsList)
                    selectedItemIdList.addAll(friendsList.map { friend -> friend.id })
                    viewModel.selectedFriendCount.value =
                        viewModel.selectedFriendCount.value?.plus(friendsList.size)
                    adapter.notifyDataSetChanged()
                }

                is UiState.Failure -> {
                    stopShimmerView()
                    toast(getString(R.string.onboarding_add_friend_error))
                }

                is UiState.Loading -> {
                    startShimmerView()
                }

                is UiState.Empty -> {
                    stopShimmerView()
                }
            }
        }
    }

    private fun startShimmerView() {
        binding.shimmerFriendList.startShimmer()
        binding.shimmerFriendList.visibility = View.VISIBLE
        binding.rvFriendList.visibility = View.GONE
        binding.shimmerTotalFriend.visibility = View.VISIBLE
        binding.btnAddFriendNext.isClickable = false
    }

    private fun stopShimmerView() {
        binding.shimmerFriendList.stopShimmer()
        binding.shimmerFriendList.visibility = View.GONE
        binding.rvFriendList.visibility = View.VISIBLE
        binding.shimmerTotalFriend.visibility = View.GONE
        binding.btnAddFriendNext.isClickable = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
    }
}
