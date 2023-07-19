package com.yello.presentation.main.profile.info

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.ProfileFriendsListModel
import com.example.domain.entity.ProfileUserModel
import com.example.ui.base.BindingFragment
import com.example.ui.fragment.toast
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
    private lateinit var friendsList: List<ProfileFriendsListModel.ProfileFriendModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initProfileManageActivityWithoutFinish()
        initFabUpwardListener()

        observeUserDataState()
        observeFriendsDataState()
        observeFriendDeleteState()

        setUserData()
        setDeleteAnimation()
        setItemDivider()
        setListWithInfinityScroll()
        setFabVisibility()
    }

    private fun observeUserDataState() {
        viewModel.getState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    initItemClickListenerWithAdapter(state.data)
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), "유저 정보 서버 통신 실패")
                }

                is UiState.Empty -> {}

                is UiState.Loading -> {}
            }
        }
    }

    private fun setUserData() {
        viewModel.getUserDataFromServer()
    }

    private fun observeFriendsDataState() {
        viewModel.getListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    friendsList = state.data?.friends ?: listOf()
                    adapter?.addItemList(friendsList)
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), "친구 목록 서버 통신 실패")
                }

                is UiState.Empty -> {}

                is UiState.Loading -> {}
            }
        }
    }

    private fun setListWithInfinityScroll() {
        binding.rvProfileFriendsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvProfileFriendsList.canScrollVertically(1) && layoutManager is LinearLayoutManager && layoutManager.findLastVisibleItemPosition() == adapter!!.itemCount - 1) {
                            viewModel.getFriendsListFromServer()
                        }
                    }
                }
            }
        })
    }

    private fun observeFriendDeleteState() {
        viewModel.deleteFriendState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    viewModel.clickedItemPosition?.let { adapter?.removeItem(it) }
                }

                is UiState.Failure -> {
                    toast("친구 삭제 실패")
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    private fun setFabVisibility() {
        binding.rvProfileFriendsList.setOnScrollChangeListener { view, _, _, _, _ ->
            // 최상단인 경우에만 GONE 표시
            binding.fabUpward.isVisible = view.canScrollVertically(-1)
        }
    }

    private fun setItemDivider() {
        binding.rvProfileFriendsList.addItemDecoration(
            ProfileItemDecoration(requireContext())
        )
    }

    private fun setDeleteAnimation() {
        binding.rvProfileFriendsList.itemAnimator = object : DefaultItemAnimator() {
            override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {

                holder.itemView.animation =
                    AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_out_right)

                return super.animateRemove(holder)
            }
        }
    }

    private fun initViewModel() {
        viewModel.currentPage = -1
        viewModel.isPagingFinish = false
        viewModel.totalPage = Int.MAX_VALUE
        viewModel.getFriendsListFromServer()
    }

    private fun initFabUpwardListener() {
        binding.fabUpward.setOnSingleClickListener {
            binding.rvProfileFriendsList.scrollToPosition(0)
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

    private fun initItemClickListenerWithAdapter(model: ProfileUserModel) {
        adapter = ProfileFriendAdapter(model) { profileUserModel, position ->

            viewModel.setItemPosition(position)
            viewModel.clickedItemId.value = profileUserModel.userId
            viewModel.clickedItemName.value = profileUserModel.name
            viewModel.clickedItemYelloId.value = "@" + profileUserModel.yelloId
            viewModel.clickedItemSchool.value = profileUserModel.group
            viewModel.clickedItemThumbnail.value = profileUserModel.profileImageUrl
            viewModel.clickedItemTotalMsg.value = profileUserModel.yelloCount.toString()
            viewModel.clickedItemTotalFriends.value = profileUserModel.friendCount.toString()

            ProfileFriendItemBottomSheet().show(parentFragmentManager, "dialog")
        }
        adapter?.setItemList(listOf())
        binding.rvProfileFriendsList.adapter = adapter
    }
}
