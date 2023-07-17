package com.yello.presentation.main.profile.info

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileBinding
import com.yello.presentation.main.profile.manage.ProfileManageActivity
import com.yello.util.context.yelloSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BindingFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel by activityViewModels<ProfileViewModel>()
    private var adapter: ProfileFriendAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initAddGroupButtonListener()
        initProfileManageActivityWithoutFinish()
        initItemClickListener()
        initFabUpwardListener()

        observeUserDataState()
        // TODO: 유저 아이디 어디에다가 저장해둔거지
        setUserData(userid = 148)
        setListToAdapterFromLocal()
        setFabVisibility()
    }

    private fun observeUserDataState() {
        viewModel.getState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    viewModel.myName.value = state.data.name
                    viewModel.myId.value = "@" + state.data.yelloId
                    viewModel.mySchool.value = state.data.group
                    // TODO: 서버통신 후 이미지도 처리하기 - 바인딩어댑터
                    // viewModel.myThumbnail.value = state.data.profileImageUrl
                    viewModel.myTotalMsg.value = state.data.yelloCount.toString()
                    viewModel.myTotalFriends.value = state.data.friendCount.toString()
                    viewModel.myTotalPoints.value = state.data.point.toString()
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), state.msg)
                }

                is UiState.Empty -> {}

                is UiState.Loading -> {}
            }
        }
    }

    private fun setUserData(userid: Int) {
        viewModel.getUserDataFromServer(userid)
    }

    private fun setFabVisibility() {
        binding.svProfile.setOnScrollChangeListener { view, _, _, _, _ ->
            // 최상단인 경우에만 GONE 표시
            binding.fabUpward.isVisible = view.canScrollVertically(-1)
        }
    }

    private fun initAddGroupButtonListener() {
        binding.btnProfileAddGroup.setOnSingleClickListener {
            // TODO: 그룹 추가 로직
        }
    }

    private fun initFabUpwardListener() {
        binding.fabUpward.setOnSingleClickListener {
            binding.svProfile.scrollTo(0, 0)
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
