package com.el.yello.presentation.onboarding.fragment.addfriend

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentAddFriendBinding
import com.el.yello.presentation.onboarding.OnBoardingViewModel
import com.el.yello.presentation.onboarding.activity.OnBoardingActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.domain.entity.onboarding.AddFriendListModel.FriendModel
import com.example.ui.base.BindingFragment
import com.example.ui.state.UiState
import com.example.ui.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    }

    override fun onResume() {
        super.onResume()
        callParentActivity {
            showBackBtn()
        }
    }

    private fun callParentActivity(callback: OnBoardingActivity.() -> Unit) {
        val activity = requireActivity()
        if (activity is OnBoardingActivity) {
            activity.callback()
        }
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
                EVENT_CLICK_ONBOARDING_NEXT,
                JSONObject().put(NAME_ONBOARD_VIEW, VALUE_FRIENDS),
            )
            (activity as? OnBoardingActivity)?.progressBarPlus()
            viewModel.selectedFriendIdList = selectedItemIdList
            findNavController().navigate(R.id.action_addFriendFragment_to_codeFragment)
        }
    }

    private fun setKakaoRecommendList() {
        setInfinityScroll()
        viewModel.initFriendPagingVariable()
        viewModel.addListWithKakaoIdList()
    }

    private fun setInfinityScroll() {
        binding.rvFriendList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvFriendList.canScrollVertically(1) && layoutManager is LinearLayoutManager && layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                            viewModel.addListWithKakaoIdList()
                        }
                    }
                }
            }
        })
    }

    private fun observeAddListState() {
        viewModel.friendListState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Success -> {
                        stopShimmerView()
                        friendsList = state.data
                        adapter.submitList(friendsList)
                        selectedItemIdList.addAll(friendsList.map { friend -> friend.id })
                        viewModel.selectedFriendCount.value = friendsList.size
                    }

                    is UiState.Failure -> {
                        stopShimmerView()
                        yelloSnackbar(
                            binding.root,
                            getString(R.string.internet_connection_error_msg)
                        )
                    }

                    is UiState.Loading -> startShimmerView()

                    is UiState.Empty -> return@onEach
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun startShimmerView() {
        with(binding) {
            shimmerFriendList.startShimmer()
            shimmerFriendList.isVisible = true
            rvFriendList.isVisible = false
            shimmerTotalFriend.isVisible = true
            btnAddFriendNext.isClickable = false
        }
    }

    private fun stopShimmerView() {
        with(binding) {
            shimmerFriendList.stopShimmer()
            shimmerFriendList.isVisible = false
            rvFriendList.isVisible = true
            shimmerTotalFriend.isVisible = false
            btnAddFriendNext.isClickable = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
    }

    companion object {
        private const val EVENT_CLICK_ONBOARDING_NEXT = "click_onboarding_next"
        private const val NAME_ONBOARD_VIEW = "onboard_view"
        private const val VALUE_FRIENDS = "friends"
    }
}
