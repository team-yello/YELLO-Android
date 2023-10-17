package com.el.yello.presentation.main.profile.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentProfileBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.el.yello.presentation.main.profile.manage.ProfileManageActivity
import com.el.yello.presentation.pay.PayActivity
import com.el.yello.util.Utils.setPullToScrollColor
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.domain.entity.ProfileUserModel
import com.example.ui.base.BindingFragment
import com.example.ui.fragment.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        setInfinityScroll()
        setDeleteAnimation()
        observeUserDataState()
        observeFriendsDataState()
        observeFriendDeleteState()
        observeCheckIsSubscribed()
        AmplitudeUtils.trackEventWithProperties("view_profile")
    }

    private fun initProfileSetting() {
        viewModel.initViewModelVariable()
        viewModel.isFirstScroll = true
        initProfileManageBtnListener()
        initUpwardBtnListener()
        initUpwardBtnVisibility()
        initAdapter()
        setItemDivider()
        viewModel.getUserDataFromServer()
        viewModel.getFriendsListFromServer()
        viewModel.getPurchaseInfoFromServer()
    }

    private fun setItemDivider() {
        itemDivider = ProfileItemDecoration(requireContext())
        binding.rvProfileFriendsList.addItemDecoration(itemDivider)
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
    private fun initAdapter() {
        _adapter = ProfileFriendAdapter((viewModel), { profileUserModel, position ->

            // 리스트 아이템 클릭 리스너 설정 - 클릭된 아이템 값 저장 뷰모델 이후 바텀 시트 출력
            viewModel.setItemPosition(position)
            viewModel.clickedUserData = profileUserModel.apply {
                if (!this.yelloId.startsWith("@")) this.yelloId = "@" + this.yelloId
            }

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
                "click_go_shop", JSONObject().put("shop_button", "profile_shop")
            )
            Intent(activity, PayActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        })
        binding.rvProfileFriendsList.adapter = adapter
    }

    private fun initPullToScrollListener() {
        binding.layoutProfileSwipe.apply {
            setOnRefreshListener {
                lifecycleScope.launch {
                    adapter.setItemList(listOf())
                    viewModel.run {
                        initViewModelVariable()
                        getPurchaseInfoFromServer()
                        getUserDataFromServer()
                        getFriendsListFromServer()
                    }
                    delay(200)
                    binding.layoutProfileSwipe.isRefreshing = false
                }
            }
            setPullToScrollColor(R.color.grayscales_500, R.color.grayscales_700)
        }
    }

    // 유저 정보 서버 통신 성공 시 어댑터 생성 후 리사이클러뷰에 부착
    private fun observeUserDataState() {
        viewModel.getUserDataState.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    viewModel.myUserData = state.data.apply {
                        if (!this.yelloId.startsWith("@")) this.yelloId = "@" + this.yelloId
                    }
                    viewModel.myFriendCount = state.data.friendCount
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), getString(R.string.profile_error_user_data))
                }

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    // 친구 목록 서버 통신 성공 시 어댑터에 리스트 추가
    private fun observeFriendsDataState() {
        viewModel.getFriendListState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Success -> {
                        binding.ivProfileLoading.isVisible = false
                        friendsList = state.data.friends
                        adapter.addItemList(friendsList)
                    }

                    is UiState.Failure -> {
                        yelloSnackbar(requireView(), getString(R.string.profile_error_friend_list))
                    }

                    is UiState.Loading -> {
                        binding.ivProfileLoading.isVisible = true
                    }

                    is UiState.Empty -> return@onEach
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    // 무한 스크롤 구현
    private fun setInfinityScroll() {
        binding.rvProfileFriendsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvProfileFriendsList.canScrollVertically(1) && layoutManager is LinearLayoutManager && layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
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
        viewModel.deleteFriendState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Success -> {
                        lifecycleScope.launch {
                            viewModel.clickedItemPosition?.let { position ->
                                adapter.removeItem(position)
                            }
                            binding.rvProfileFriendsList.removeItemDecoration(itemDivider)
                            delay(450)
                            binding.rvProfileFriendsList.addItemDecoration(itemDivider)
                            viewModel.myFriendCount -= 1
                            adapter.notifyDataSetChanged()
                        }
                        AmplitudeUtils.trackEventWithProperties("complete_profile_delete_friend")
                    }

                    is UiState.Failure -> toast(getString(R.string.profile_error_delete_friend))

                    is UiState.Loading -> return@onEach

                    is UiState.Empty -> return@onEach
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    // 구독 여부 확인
    private fun observeCheckIsSubscribed() {
        viewModel.getPurchaseInfoState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Success -> viewModel.isSubscribed = state.data.isSubscribe == true

                    is UiState.Failure -> viewModel.isSubscribed = false

                    is UiState.Loading -> return@onEach

                    is UiState.Empty -> return@onEach
                }
                adapter.notifyDataSetChanged()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
    }

    private companion object {
        const val ITEM_BOTTOM_SHEET = "itemBottomSheet"
        const val ADD_GROUP_URL = "https://bit.ly/44xDDqC"
    }
}
