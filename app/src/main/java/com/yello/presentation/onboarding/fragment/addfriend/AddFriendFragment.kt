package com.yello.presentation.onboarding.fragment.addfriend

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.domain.entity.onboarding.Friend
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentAddfreindBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel

class AddFriendFragment : BindingFragment<FragmentAddfreindBinding>(R.layout.fragment_addfreind) {

    private lateinit var friendList: List<Friend>
    private var adapter: AddFriendAdapter? = null

    private val viewModel by activityViewModels<OnBoardingViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initFriendAdapter()
        setConfirmBtnClickListener()
        setBackBtnClickListener()
        observe()
    }

    private fun initFriendAdapter() {
        friendList = viewModel.friendResult.value ?: emptyList()
        adapter = AddFriendAdapter { friend, position ->
            friend.isSelected = !friend.isSelected
            adapter?.notifyItemChanged(position)
        }
        binding.rvFreindList.adapter = adapter
        adapter?.submitList(friendList)
    }

    private fun setConfirmBtnClickListener() {
        binding.btnAddfriendNext.setOnSingleClickListener {
            viewModel.navigateToNextPage()
        }
    }
    private fun setBackBtnClickListener() {
        binding.btnAddfriendBackBtn.setOnSingleClickListener {
            viewModel.navigateToBackPage()
        }
    }

    private fun observe() {
        viewModel.friendData.observe(viewLifecycleOwner) {
            adapter?.submitList(friendList)
        }
    }

    fun storeFriend(friend: Friend) {
        viewModel.setFriend(friend)
    }
    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }
}
