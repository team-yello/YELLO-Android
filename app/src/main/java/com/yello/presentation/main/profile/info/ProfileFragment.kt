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
import com.yello.presentation.main.profile.ProfileViewModel
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
        observeFriendsDataState()
        // TODO: 유저 아이디 어디에다가 저장해둔거지
        setUserData(userid = 148)
        setFriendsData(0)
        setFabVisibility()
    }

    private fun observeUserDataState() {
        viewModel.getState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    viewModel.myName.value = state.data?.name
                    viewModel.myId.value = "@" + state.data?.yelloId
                    viewModel.mySchool.value = state.data?.group
                    // TODO: 서버통신 후 이미지도 처리하기 - 바인딩어댑터
                    // viewModel.myThumbnail.value = state.data.profileImageUrl
                    viewModel.myTotalMsg.value = state.data?.yelloCount.toString()
                    viewModel.myTotalFriends.value = state.data?.friendCount.toString()
                    viewModel.myTotalPoints.value = state.data?.point.toString()
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), "유저 정보 서버 통신 실패")
                }

                is UiState.Empty -> {}

                is UiState.Loading -> {}
            }
        }
    }

    private fun setUserData(userid: Int) {
        viewModel.getUserDataFromServer(userid)
    }

    private fun observeFriendsDataState() {
        viewModel.getListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    val friendsList = state.data?.friends ?: listOf()
                    binding.rvProfileFriendsList.adapter = adapter?.apply {
                        setItemList(friendsList)
                    }
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), "친구 목록 서버 통신 실패")
                }

                is UiState.Empty -> {}

                is UiState.Loading -> {}
            }
        }
    }

    private fun setFriendsData(page: Int) {
        viewModel.getFriendsDataFromServer(page)
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
        adapter = ProfileFriendAdapter { profileUserModel ->

            viewModel.clickedItemId.value = profileUserModel.userId
            viewModel.clickedItemName.value = profileUserModel.name
            viewModel.clickedItemYelloId.value = "@" + profileUserModel.yelloId
            viewModel.clickedItemSchool.value = profileUserModel.group
            // TODO: 서버통신 후 이미지도 처리하기 - 바인딩어댑터
            // viewModel.clickedItemThumbnail.value = profileUserModel.profileImageUrl
            viewModel.clickedItemTotalMsg.value = profileUserModel.yelloCount.toString()
            viewModel.clickedItemTotalFriends.value = profileUserModel.friendCount.toString()

            ProfileFriendItemBottomSheet().show(parentFragmentManager, "dialog")
        }
    }
}
