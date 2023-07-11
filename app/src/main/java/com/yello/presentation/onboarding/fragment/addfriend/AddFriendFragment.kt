package com.yello.presentation.onboarding.fragment.addfriend

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.domain.entity.MyFriend
import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentAddfreindBinding

class AddFriendFragment : BindingFragment<FragmentAddfreindBinding>(R.layout.fragment_addfreind) {

    private lateinit var friendList: List<MyFriend>

    private val viewModel by viewModels<AddFriendViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFriendAdapter()
    }

    private fun initFriendAdapter() {
        viewModel.addFriend()
        friendList = viewModel.friendResult.value ?: emptyList()
        val adapter = AddFriendAdapter(requireContext())
        binding.rvFreindList.adapter = adapter

        adapter.submitList(friendList)
    }
}
