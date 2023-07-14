package com.yello.presentation.main.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileBinding

class ProfileFragment : BindingFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel by activityViewModels<ProfileViewModel>()
    private var adapter: ProfileFriendAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        binding.btnProfileAddGroup.setOnSingleClickListener {
            // TODO: 그룹 추가 로직
        }

        initProfileManageActivityWithoutFinish()
        initItemClickListener()
        setMyProfileData()
        setListToAdapterFromLocal()

        setFabVisibility()
        initFabUpwardListener()

    }

    private fun setMyProfileData() {
        viewModel.myName.value = "김상호"
        viewModel.myId.value = "@sangho.kk"
        viewModel.mySchool.value = "고려대학교 산업경영공학부 19학번"
        // TODO: 서버통신 후 이미지도 처리하기 - 바인딩어댑터
        viewModel.myThumbnail.value = ""
        viewModel.myTotalMsg.value = "31"
        viewModel.myTotalFriends.value = "95"
        viewModel.myTotalPoints.value = "930"
    }

    private fun setFabVisibility() {
        binding.svProfile.setOnScrollChangeListener { view, _, _, _, _ ->
            // 최상단인 경우에만 GONE 표시
            if (view.canScrollVertically(-1)) {
                binding.fabUpward.visibility = View.VISIBLE
            } else {
                binding.fabUpward.visibility = View.GONE
            }
        }
    }

    private fun initFabUpwardListener() {
        binding.fabUpward.setOnSingleClickListener {
            binding.svProfile.fullScroll(ScrollView.FOCUS_UP)
        }
    }

    private fun initProfileManageActivityWithoutFinish() {
        binding.btnProfileManage.setOnSingleClickListener {
            Intent(activity, ProfileManageActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }

    private fun initItemClickListener() {
        adapter = ProfileFriendAdapter { profileFriendModel ->

            viewModel.clickedItemName.value = profileFriendModel.name
            viewModel.clickedItemId.value = profileFriendModel.yelloId
            viewModel.clickedItemSchool.value = profileFriendModel.school
            // TODO: 서버통신 후 이미지도 처리하기 - 바인딩어댑터
            viewModel.clickedItemThumbnail.value = profileFriendModel.thumbnail
            viewModel.clickedItemTotalMsg.value = profileFriendModel.totalMsg.toString()
            viewModel.clickedItemTotalFriends.value = profileFriendModel.totalFriends.toString()

            Log.d("sangho", "$viewModel")
            ProfileFriendItemBottomSheet().show(parentFragmentManager, "dialog")
        }
    }

    private fun setListToAdapterFromLocal() {
        viewModel.addListFromLocal()
        val friendsList = viewModel.friendsResult.value ?: emptyList()
        binding.rvProfileFriendsList.adapter = adapter?.apply {
            setItemList(friendsList)
        }
    }
}
