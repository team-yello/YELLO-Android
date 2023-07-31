package com.el.yello.presentation.onboarding.fragment.addfriend

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentAddfreindBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.example.domain.entity.onboarding.Friend
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFriendFragment : BindingFragment<FragmentAddfreindBinding>(R.layout.fragment_addfreind) {

    private var adapter: AddFriendAdapter? = null

    private val viewModel by activityViewModels<OnBoardingViewModel>()
    private lateinit var friendsList: List<Friend>

    private var selectedItemIdList = mutableListOf<Long>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initFriendAdapter()
        setConfirmBtnClickListener()
        setBackBtnClickListener()
        setKakaoRecommendList()
        observeAddListState()
    }

    private fun initFriendAdapter() {
        adapter = AddFriendAdapter { friend, position ->
            friend.isSelected = !friend.isSelected
            if (friend.isSelected && friend.id !in selectedItemIdList) {
                selectedItemIdList.add(friend.id)
                viewModel.selectedFriendCount.value = viewModel.selectedFriendCount.value?.plus(1)
            } else {
                selectedItemIdList.remove(friend.id)
                viewModel.selectedFriendCount.value = viewModel.selectedFriendCount.value?.minus(1)
            }
            adapter?.notifyItemChanged(position)
        }
        binding.rvFreindList.adapter = adapter
    }

    private fun setConfirmBtnClickListener() {
        binding.btnAddfriendNext.setOnSingleClickListener {
            viewModel.selectedFriendIdList = selectedItemIdList
            viewModel.navigateToNextPage()
        }
    }

    private fun setBackBtnClickListener() {
        binding.btnAddfriendBackBtn.setOnSingleClickListener {
            viewModel.navigateToBackPage()
        }
    }

    // 서버 통신 성공 시 카카오 추천 친구 추가
    private fun setKakaoRecommendList() {
        setListWithInfinityScroll()
        viewModel.initFriendPagingVariable()
        viewModel.addListWithKakaoIdList()
    }

    // 무한 스크롤 구현
    private fun setListWithInfinityScroll() {
        binding.rvFreindList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvFreindList.canScrollVertically(1)
                            && layoutManager is LinearLayoutManager
                            && layoutManager.findLastVisibleItemPosition() == adapter!!.itemCount - 1
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
        viewModel.friendListState.observe(viewLifecycleOwner) {
            friendsList = it.friendList
            adapter?.submitList(friendsList)
            selectedItemIdList.addAll(friendsList.map { friend -> friend.id })
            viewModel.selectedFriendCount.value =
                viewModel.selectedFriendCount.value?.plus(friendsList.size)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }
}
