package com.yello.presentation.main.profile.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var friendsList: List<ProfileUserModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initItemClickListenerWithAdapter()
        initProfileSetting()
        setUserDataFromServer()
        setFriendsListDataFromServer()
        setFriendDeleteToServer()
    }

    private fun initProfileSetting() {
        initViewModelWithFirstList()
        initProfileManageActivityWithoutFinish()
        initFabUpwardListener()
        setFabVisibility()
    }

    private fun setUserDataFromServer() {
        observeUserDataState()
        setUserData()
    }

    private fun setFriendsListDataFromServer() {
        observeFriendsDataState()
        setListWithInfinityScroll()
        setItemDivider()
    }

    private fun setFriendDeleteToServer() {
        observeFriendDeleteState()
        setDeleteAnimation()
    }

    // 프래그먼트 생성될 때마다 뷰모델 변수값 초기화
    private fun initViewModelWithFirstList() {
        viewModel.currentPage = -1
        viewModel.isPagingFinish = false
        viewModel.totalPage = Int.MAX_VALUE
        viewModel.getFriendsListFromServer()
        viewModel.getFriendsListFromServer()
    }

    // 관리 액티비티 실행 & 뒤로가기 누를 때 다시 돌아오도록 현재 화면 finish 진행 X
    private fun initProfileManageActivityWithoutFinish() {
        binding.btnProfileManage.setOnSingleClickListener {
            Intent(activity, ProfileManageActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }

    // 플로팅 버튼 클릭 시 리사이클러 최상단으로 이동
    private fun initFabUpwardListener() {
        binding.fabUpward.setOnSingleClickListener {
            binding.rvProfileFriendsList.scrollToPosition(0)
        }
    }

    // 최상단에 위치한 경우에만 플로팅 버튼 GONE 표시
    private fun setFabVisibility() {
        binding.rvProfileFriendsList.setOnScrollChangeListener { view, _, _, _, _ ->
            binding.fabUpward.isVisible = view.canScrollVertically(-1)
        }
    }

    // 유저 정보 서버 통신 성공 시 어댑터 생성 후 리사이클러뷰에 부착
    private fun observeUserDataState() {
        viewModel.getState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    // 서버 통신으로 받은 유저 정보 뷰모델에 저장
                    viewModel.myName.value = state.data.name
                    viewModel.myId.value = "@" + state.data.yelloId
                    viewModel.mySchool.value = state.data.group
                    viewModel.myThumbnail.value = state.data.profileImageUrl
                    viewModel.myTotalMsg.value = state.data.yelloCount.toString()
                    viewModel.myTotalFriends.value = state.data.friendCount.toString()
                    viewModel.myTotalPoints.value = state.data.point.toString()
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), getString(R.string.profile_error_user_data))
                }

                is UiState.Empty -> {}

                is UiState.Loading -> {}
            }
        }
    }

    private fun setUserData() {
        viewModel.getUserDataFromServer()
    }

    // 어댑터 시작
    private fun initItemClickListenerWithAdapter() {
        adapter = ProfileFriendAdapter((viewModel), { profileUserModel, position ->

            // 아이템 클릭 리스너 설정 - 클릭된 아이템 값 저장 뷰모델 이후 바텀 시트 출력
            viewModel.setItemPosition(position)
            viewModel.clickedItemId.value = profileUserModel.userId
            viewModel.clickedItemName.value = profileUserModel.name
            viewModel.clickedItemYelloId.value = "@" + profileUserModel.yelloId
            viewModel.clickedItemSchool.value = profileUserModel.group
            viewModel.clickedItemThumbnail.value = profileUserModel.profileImageUrl
            viewModel.clickedItemTotalMsg.value = profileUserModel.yelloCount.toString()
            viewModel.clickedItemTotalFriends.value = profileUserModel.friendCount.toString()

            ProfileFriendItemBottomSheet().show(parentFragmentManager, DIALOG)
        }, {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("bit.ly/44xDDqC")))
        })
        adapter?.setItemList(listOf())
        binding.rvProfileFriendsList.adapter = adapter
    }

    // 친구 목록 서버 통신 성공 시 어댑터에 리스트 추가
    private fun observeFriendsDataState() {
        viewModel.getListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    friendsList = state.data?.friends ?: listOf()
                    adapter?.addItemList(friendsList)
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), getString(R.string.profile_error_friend_list))
                }

                is UiState.Empty -> {}

                is UiState.Loading -> {}
            }
        }
    }

    // 무한 스크롤 구현
    private fun setListWithInfinityScroll() {
        binding.rvProfileFriendsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvProfileFriendsList.canScrollVertically(1)
                            && layoutManager is LinearLayoutManager
                            && layoutManager.findLastVisibleItemPosition() == adapter!!.itemCount - 1
                        ) {
                            viewModel.getFriendsListFromServer()
                        }
                    }
                }
            }
        })
    }

    // 리스트 디바이더 설정
    private fun setItemDivider() {
        binding.rvProfileFriendsList.addItemDecoration(
            ProfileItemDecoration(requireContext())
        )
    }

    // 친구 삭제 서버 통신 성공 시 리스트에서 아이템 삭제
    private fun observeFriendDeleteState() {
        viewModel.deleteFriendState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    viewModel.clickedItemPosition?.let { position -> adapter?.removeItem(position) }
                    if (viewModel.myTotalFriends.value != "") {
                        viewModel.myTotalFriends.value =
                            viewModel.myTotalFriends.value?.toInt()?.minus(1).toString()
                        adapter?.notifyDataSetChanged()
                    }
                }

                is UiState.Failure -> {
                    toast(getString(R.string.profile_error_delete_friend))
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    // 친구 삭제 시 오른쪽으로 스와이프 되는 애니메이션 추가
    private fun setDeleteAnimation() {
        binding.rvProfileFriendsList.itemAnimator = object : DefaultItemAnimator() {
            override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {

                holder.itemView.animation =
                    AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_out_right)

                return super.animateRemove(holder)
            }
        }
    }

    private companion object {
        const val DIALOG = "dialog"
    }
}