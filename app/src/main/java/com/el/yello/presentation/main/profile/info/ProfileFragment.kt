package com.el.yello.presentation.main.profile.info

import android.app.Activity
import android.content.Intent
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

    private var profileFriendItemBottomSheet: ProfileFriendItemBottomSheet? = null

    private var isScrolled: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProfileSetting()
        initPullToScrollListener()
        setInfinityScroll()
        setDeleteAnimation()
        observeUserDataResult()
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

    private fun initProfileManageBtnListener() {
        binding.btnProfileManage.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_profile_manage")
            navigateTo<ProfileManageActivity>()
        }
    }

    private fun initUpwardBtnListener() {
        binding.fabUpward.setOnSingleClickListener {
            binding.rvProfileFriendsList.scrollToPosition(0)
        }
    }

    private fun initUpwardBtnVisibility() {
        binding.rvProfileFriendsList.setOnScrollChangeListener { view, _, _, _, _ ->
            binding.fabUpward.isVisible = view.canScrollVertically(-1)
        }
    }

    private fun initAdapter() {
        _adapter = ProfileFriendAdapter(viewModel,
            itemClick = { profileUserModel, position -> initItemClickListener(profileUserModel, position) },
            shopClick = { initShopClickListener() },
            modClick = { initProfileModClickListener() }
        )
        binding.rvProfileFriendsList.adapter = adapter
    }

    private fun initItemClickListener(profileUserModel: ProfileUserModel, position: Int) {
        viewModel.setItemPosition(position)
        viewModel.clickedUserData = profileUserModel.apply {
            if (!this.yelloId.startsWith("@")) this.yelloId = "@" + this.yelloId
        }
        if (!viewModel.isItemBottomSheetRunning) {
            AmplitudeUtils.trackEventWithProperties("click_profile_friend")
            profileFriendItemBottomSheet = ProfileFriendItemBottomSheet()
            profileFriendItemBottomSheet?.show(parentFragmentManager, ITEM_BOTTOM_SHEET)
        }
    }

    private fun initShopClickListener() {
        AmplitudeUtils.trackEventWithProperties(
            "click_go_shop",
            JSONObject().put("shop_button", "profile_shop"),
        )
        navigateTo<PayActivity>()
    }

    private fun initProfileModClickListener() {
        when (viewModel.myUserData.groupType) {
            TYPE_UNIVERSITY -> {}
            TYPE_HIGH_SCHOOL -> {}
            TYPE_MIDDLE_SCHOOL -> {}
            else -> toast(getString(R.string.sign_in_error_connection))
        }
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

    private fun observeUserDataResult() {
        viewModel.getUserDataResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) yelloSnackbar(requireView(), getString(R.string.profile_error_user_data))
        }.launchIn(lifecycleScope)
    }

    private fun observeFriendsDataState() {
        viewModel.getFriendListState.flowWithLifecycle(lifecycle).onEach { state ->
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
        }.launchIn(lifecycleScope)
    }

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

    private fun observeFriendDeleteState() {
        viewModel.deleteFriendState.flowWithLifecycle(lifecycle).onEach { state ->
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
        }.launchIn(lifecycleScope)
    }

    private fun observeCheckIsSubscribed() {
        viewModel.getPurchaseInfoState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> viewModel.isSubscribed = state.data.isSubscribe == true

                is UiState.Failure -> viewModel.isSubscribed = false

                is UiState.Loading -> return@onEach

                is UiState.Empty -> return@onEach
            }
            adapter.notifyDataSetChanged()
        }.launchIn(lifecycleScope)
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

    fun scrollToTop() {
        binding.rvProfileFriendsList.smoothScrollToPosition(0)
    }

    private inline fun <reified T : Activity>navigateTo() {
        Intent(activity, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
        if (profileFriendItemBottomSheet != null) profileFriendItemBottomSheet?.dismiss()
    }

    private companion object {
        const val ITEM_BOTTOM_SHEET = "itemBottomSheet"

        const val TYPE_UNIVERSITY = "UNIVERSITY"
        const val TYPE_HIGH_SCHOOL = "HIGH_SCHOOL"
        const val TYPE_MIDDLE_SCHOOL = "MIDDLE_SCHOOL"
    }
}
