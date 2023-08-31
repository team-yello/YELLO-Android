package com.el.yello.presentation.main.profile.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentProfileBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.presentation.main.profile.manage.ProfileManageActivity
import com.el.yello.presentation.pay.PayActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.domain.entity.ProfileUserModel
import com.example.ui.base.BindingFragment
import com.example.ui.fragment.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class ProfileFragment : BindingFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private var _adapter: ProfileFriendAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private val viewModel by activityViewModels<ProfileViewModel>()

    private lateinit var friendsList: List<ProfileUserModel>

    private lateinit var itemDivider: ProfileItemDecoration

    private var isScrolled: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProfileSetting()
        initPullToScrollListener()
        setItemDivider()
        setUserDataFromServer()
        setFriendsListDataFromServer()
        setFriendDeleteToServer()
        setIsSubscribedFromServer()
        AmplitudeUtils.trackEventWithProperties("view_profile")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
    }

    private fun initProfileSetting() {
        viewModel.initPagingVariable()
        viewModel.isFirstScroll = true
        initProfileManageBtnListener()
        initUpwardBtnListener()
        initUpwardBtnVisibility()
        initAdapterWithClickListener()
    }

    private fun setItemDivider() {
        itemDivider = ProfileItemDecoration(requireContext())
        binding.rvProfileFriendsList.addItemDecoration(itemDivider)
    }

    private fun setUserDataFromServer() {
        observeUserDataState()
        viewModel.getUserDataFromServer()
    }

    private fun setFriendsListDataFromServer() {
        observeFriendsDataState()
        setListWithInfinityScroll()
        viewModel.getFriendsListFromServer()
    }

    private fun setFriendDeleteToServer() {
        observeFriendDeleteState()
        setDeleteAnimation()
    }

    private fun setIsSubscribedFromServer() {
        observeCheckIsSubscribed()
        viewModel.getPurchaseInfoFromServer()
    }

    // 관리 액티비티 실행 & 뒤로가기 누를 때 다시 돌아오도록 현재 화면 finish 진행 X
    private fun initProfileManageBtnListener() {
        binding.btnProfileManage.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_profile_manage")
            Intent(activity, ProfileManageActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }
    }

    // 플로팅 버튼 클릭 시 리사이클러 최상단으로 이동
    private fun initUpwardBtnListener() {
        binding.fabUpward.setOnSingleClickListener {
            binding.rvProfileFriendsList.scrollToPosition(0)
        }
    }

    // 최상단에 위치한 경우에만 플로팅 버튼 GONE 표시
    private fun initUpwardBtnVisibility() {
        binding.rvProfileFriendsList.setOnScrollChangeListener { view, _, _, _, _ ->
            binding.fabUpward.isVisible = view.canScrollVertically(-1)
        }
    }

    // 어댑터 시작
    private fun initAdapterWithClickListener() {
        _adapter = ProfileFriendAdapter((viewModel), { profileUserModel, position ->

            // 리스트 아이템 클릭 리스너 설정 - 클릭된 아이템 값 저장 뷰모델 이후 바텀 시트 출력
            viewModel.setItemPosition(position)
            viewModel.clickedItemId.value = profileUserModel.userId
            viewModel.clickedItemName.value = profileUserModel.name
            viewModel.clickedItemYelloId.value = "@" + profileUserModel.yelloId
            viewModel.clickedItemSchool.value = profileUserModel.group
            viewModel.clickedItemThumbnail.value = profileUserModel.profileImageUrl
            viewModel.clickedItemTotalMsg.value = profileUserModel.yelloCount.toString()
            viewModel.clickedItemTotalFriends.value = profileUserModel.friendCount.toString()

            if (!viewModel.isItemBottomSheetRunning) {
                AmplitudeUtils.trackEventWithProperties("click_profile_friend")
                ProfileFriendItemBottomSheet().show(
                    parentFragmentManager, ITEM_BOTTOM_SHEET
                )
            }
        }, {
            // 헤더 그룹 추가 버튼 클릭 리스너 설정
            AmplitudeUtils.trackEventWithProperties("click_profile_group")
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ADD_GROUP_URL)))
        }, {
            // 헤더 상점 버튼 클릭 리스너 설정
            AmplitudeUtils.trackEventWithProperties(
                "click_go_shop",
                JSONObject().put("shop_button", "profile_shop")
            )
            Intent(activity, PayActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        })
        adapter.setItemList(listOf())
        binding.rvProfileFriendsList.adapter = adapter
    }

    private fun initPullToScrollListener() {
        binding.layoutProfileSwipe.apply {
            setOnRefreshListener {
                lifecycleScope.launch {
                    adapter.setItemList(listOf())
                    viewModel.initPagingVariable()
                    viewModel.getPurchaseInfoFromServer()
                    viewModel.getUserDataFromServer()
                    viewModel.getFriendsListFromServer()
                    delay(200)
                    binding.layoutProfileSwipe.isRefreshing = false
                }
            }
            setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context, R.color.grayscales_700))
            setColorSchemeColors(ContextCompat.getColor(context, R.color.grayscales_500))
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

    // 친구 목록 서버 통신 성공 시 어댑터에 리스트 추가
    private fun observeFriendsDataState() {
        viewModel.getListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    binding.ivProfileLoading.visibility = View.GONE
                    friendsList = state.data?.friends ?: listOf()
                    adapter.addItemList(friendsList)
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), getString(R.string.profile_error_friend_list))
                }

                is UiState.Loading -> {
                    binding.ivProfileLoading.visibility = View.VISIBLE
                }

                is UiState.Empty -> {}
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
                            && layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1
                        ) {
                            viewModel.getFriendsListFromServer()
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isScrolled) {
                    AmplitudeUtils.trackEventWithProperties("scroll_profile_friends")
                    isScrolled = true
                }
            }
        })
    }

    // 친구 삭제 서버 통신 성공 시 리스트에서 아이템 삭제
    private fun observeFriendDeleteState() {
        viewModel.deleteFriendState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    lifecycleScope.launch {
                        viewModel.clickedItemPosition?.let { position ->
                            adapter.removeItem(
                                position
                            )
                        }
                        binding.rvProfileFriendsList.removeItemDecoration(itemDivider)
                        delay(450)
                        binding.rvProfileFriendsList.addItemDecoration(itemDivider)
                        if (viewModel.myTotalFriends.value != "") {
                            viewModel.myTotalFriends.value =
                                viewModel.myTotalFriends.value?.toInt()?.minus(1).toString()
                            adapter.notifyDataSetChanged()
                        }
                    }
                    AmplitudeUtils.trackEventWithProperties("complete_profile_delete_friend")
                }

                is UiState.Failure -> {
                    toast(getString(R.string.profile_error_delete_friend))
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    // 구독 여부 확인
    private fun observeCheckIsSubscribed() {
        viewModel.getPurchaseInfoState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    viewModel.isSubscribed = state.data?.isSubscribe == true
                }

                is UiState.Failure -> {
                    viewModel.isSubscribed = false
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
            adapter.notifyDataSetChanged()
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

    fun scrollToTop() {
        binding.rvProfileFriendsList.smoothScrollToPosition(0)
    }

    private companion object {
        const val ITEM_BOTTOM_SHEET = "itemBottomSheet"
        const val ADD_GROUP_URL = "https://bit.ly/44xDDqC"
    }
}
