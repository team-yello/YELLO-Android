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
    private var adapter: ProfileFriendAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnProfileAddGroup.setOnSingleClickListener {
            // TODO: 그룹 추가 로직
        }
        initProfileManageActivityWithoutFinish()
        setListToAdapterFromLocal()
    }

    private fun initProfileManageActivityWithoutFinish() {
        binding.btnProfileManage.setOnSingleClickListener {
            Intent(activity, ProfileManageActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }

    private fun setListToAdapterFromLocal() {
        viewModel.addListFromLocal()
        val friendsList = viewModel.friendsResult.value ?: emptyList()
        adapter = ProfileFriendAdapter { profileFriendModel ->

            val name = profileFriendModel.name
            val yelloId = profileFriendModel.yelloId
            val school = profileFriendModel.school
            val thumbnail = profileFriendModel.thumbnail ?: ""
            ProfileFriendItemBottomSheet.newInstance(name, yelloId, school, thumbnail)
                .show(parentFragmentManager, "dialog")
        }
        binding.rvProfileFriendsList.adapter = adapter?.apply {
            setItemList(friendsList)
        }
    }
}
