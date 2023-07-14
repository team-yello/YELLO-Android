package com.yello.presentation.onboarding.fragment.addfriend

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.domain.entity.MyFriend
import com.example.ui.base.BindingFragment
import com.example.ui.fragment.toast
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentAddfreindBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel

class AddFriendFragment : BindingFragment<FragmentAddfreindBinding>(R.layout.fragment_addfreind) {

    private lateinit var friendList: List<MyFriend>
    private var adapter: AddFriendAdapter? = null

    private val viewModel by activityViewModels<OnBoardingViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFriendAdapter()
        setConfirmBtnClickListener()
        setBackBtnClickListener()
    }

    private fun initFriendAdapter() {
        viewModel.addFriend()
        friendList = viewModel.friendResult.value ?: emptyList()
        adapter = AddFriendAdapter { friend, position ->
            toast("asdf")
            friend.isSelcted = !friend.isSelcted
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
    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }
}
