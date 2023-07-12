package com.yello.presentation.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileBinding

class ProfileFragment : BindingFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel by viewModels<ProfileViewModel>()
    private val profileFriendItemBottomSheet: ProfileFriendItemBottomSheet =
        ProfileFriendItemBottomSheet()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnProfileAddGroup.setOnSingleClickListener {
            // TODO: 그룹 추가 로직
            // TODO: 바텀시트 추가
            // TODO: 플로팅버튼
            // profileFriendItemBottomSheet.show(parentFragmentManager, "Dialog")
        }
        initProfileManageActivityWithoutFinish()
        setListToAdapterFromLocal()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissDialog()
    }

    private fun initProfileManageActivityWithoutFinish() {
        binding.btnProfileManage.setOnSingleClickListener {
            Intent(activity, ProfileManageActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }

    private fun dismissDialog() {
        if (profileFriendItemBottomSheet.isAdded) profileFriendItemBottomSheet.dismiss()
    }

    private fun setListToAdapterFromLocal() {
        viewModel.addListFromLocal()
        val friendsList = viewModel.friendsResult.value ?: emptyList()
        binding.rvProfileFriendsList.adapter = ProfileFriendAdapter(requireContext()).apply {
            setItemList(friendsList)
        }
    }
}
