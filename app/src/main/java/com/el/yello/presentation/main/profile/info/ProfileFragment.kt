package com.el.yello.presentation.main.profile.info

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentProfileBinding
import com.el.yello.presentation.main.profile.detail.ProfileDetailActivity
import com.el.yello.presentation.pay.PayActivity
import com.el.yello.presentation.setting.SettingActivity
import com.el.yello.util.extension.setPullToScrollColor
import com.el.yello.util.extension.yelloSnackbar
import com.el.yello.util.manager.AmplitudeManager
import com.example.domain.entity.ProfileUserModel
import com.example.ui.base.BindingFragment
import com.example.ui.extension.navigateTo
import com.example.ui.extension.setOnSingleClickListener
import com.example.ui.state.UiState
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

        initSettingBtnListener()
        initUpwardBtnListener()
        initUpwardBtnVisibility()
        initAdapter()
        initPullToScrollListener()
        setItemDivider()
        setInfinityScroll()
        setDeleteAnimation()
        observeUserDataResult()
        observeFriendsDataState()
        observeFriendDeleteState()
        observeCheckIsSubscribed()
        observeGetBannerState()
        AmplitudeManager.trackEventWithProperties(EVENT_VIEW_PROFILE)
    }

    override fun onResume() {
        super.onResume()

        adapter.setItemList(listOf())
        viewModel.resetProfileData()
    }

    private fun initSettingBtnListener() {
        binding.btnProfileManage.setOnSingleClickListener {
            AmplitudeManager.trackEventWithProperties(EVENT_CLICK_PROFILE_MANAGE)
            activity?.navigateTo<SettingActivity>()
            viewModel.resetStateVariable()
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
        _adapter = ProfileFriendAdapter(
            viewModel,
            itemClick = ::initItemClickListener,
            shopClick = ::initShopClickListener,
            modClick = ::initProfileModClickListener,
            bannerClick = ::initProfileBannerClickListener,
            setHeight = ::setSpinnerHeight,
        )
        binding.rvProfileFriendsList.adapter = adapter
    }

    private fun initItemClickListener(profileUserModel: ProfileUserModel, position: Int) {
        viewModel.setItemPosition(position)
        viewModel.clickedUserData = profileUserModel.apply {
            if (!this.yelloId.startsWith("@")) this.yelloId = "@" + this.yelloId
        }
        if (!viewModel.isItemBottomSheetRunning) {
            AmplitudeManager.trackEventWithProperties(EVENT_CLICK_PROFILE_FRIEND)
            profileFriendItemBottomSheet = ProfileFriendItemBottomSheet()
            profileFriendItemBottomSheet?.show(parentFragmentManager, ITEM_BOTTOM_SHEET)
        }
    }

    private fun initShopClickListener(unit: Unit) {
        AmplitudeManager.trackEventWithProperties(
            EVENT_CLICK_GO_SHOP,
            JSONObject().put(NAME_SHOP_BUTTON, VALUE_PROFILE_SHOP),
        )
        activity?.navigateTo<PayActivity>()
        viewModel.resetStateVariable()
    }

    private fun initProfileModClickListener(unit: Unit) {
        AmplitudeManager.trackEventWithProperties(EVENT_CLICK_PROFILE_MY_INFO)
        activity?.navigateTo<ProfileDetailActivity>()
        viewModel.resetStateVariable()
    }

    private fun initProfileBannerClickListener(redirectUrl: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl)))
    }

    private fun initPullToScrollListener() {
        binding.layoutProfileSwipe.apply {
            setOnRefreshListener {
                lifecycleScope.launch {
                    adapter.setItemList(listOf())
                    viewModel.resetProfileData()
                    delay(200)
                    binding.layoutProfileSwipe.isRefreshing = false
                }
            }
            setPullToScrollColor(R.color.grayscales_500, R.color.grayscales_700)
        }
    }

    private fun setItemDivider() {
        itemDivider = ProfileItemDecoration(requireContext())
        binding.rvProfileFriendsList.addItemDecoration(itemDivider)
    }

    private fun setDeleteAnimation() {
        binding.rvProfileFriendsList.itemAnimator = object : DefaultItemAnimator() {
            override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
                holder.itemView.animation = AnimationUtils.loadAnimation(
                    holder.itemView.context,
                    R.anim.slide_out_right,
                )
                return super.animateRemove(holder)
            }
        }
    }

    private fun observeUserDataResult() {
        viewModel.getUserDataResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) {
                yelloSnackbar(requireView(), getString(R.string.internet_connection_error_msg))
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeFriendsDataState() {
        viewModel.getFriendListState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.ivProfileLoading.isVisible = false
                    friendsList = state.data.friends
                    binding.tvProfileNoFriend.isVisible = friendsList.isEmpty()
                    adapter.addItemList(friendsList)
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), getString(R.string.internet_connection_error_msg))
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
                    AmplitudeManager.trackEventWithProperties(EVENT_SCROLL_PROFILE_FRIENDS)
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
                        binding.tvProfileNoFriend.isVisible = viewModel.myFriendCount == 0
                        adapter.notifyDataSetChanged()
                    }
                    AmplitudeManager.trackEventWithProperties(EVENT_COMPLETE_PROFILE_DELETE_FRIEND)
                }

                is UiState.Failure -> yelloSnackbar(
                    binding.root,
                    getString(R.string.internet_connection_error_msg),
                )

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

    private fun observeGetBannerState() {
        viewModel.getBannerResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) {
                yelloSnackbar(binding.root, getString(R.string.internet_connection_error_msg))
            }
            viewModel.getFriendsListFromServer()
        }.launchIn(lifecycleScope)
    }

    private fun setSpinnerHeight(userInfoHeight: Int) {
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        binding.ivProfileLoading.layoutParams =
            (binding.ivProfileLoading.layoutParams as ConstraintLayout.LayoutParams).apply {
                bottomMargin = (screenHeight - userInfoHeight) / 2
            }
        binding.tvProfileNoFriend.layoutParams =
            (binding.tvProfileNoFriend.layoutParams as ConstraintLayout.LayoutParams).apply {
                bottomMargin = (screenHeight - userInfoHeight) / 2
            }
    }

    fun scrollToTop() {
        binding.rvProfileFriendsList.smoothScrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
        if (profileFriendItemBottomSheet != null) profileFriendItemBottomSheet?.dismiss()
    }

    companion object {
        const val ITEM_BOTTOM_SHEET = "itemBottomSheet"

        const val TYPE_UNIVERSITY = "UNIVERSITY"
        const val TYPE_HIGH_SCHOOL = "HIGH_SCHOOL"
        const val TYPE_MIDDLE_SCHOOL = "MIDDLE_SCHOOL"

        private const val EVENT_VIEW_PROFILE = "view_profile"
        private const val EVENT_CLICK_PROFILE_MANAGE = "click_profile_manage"
        private const val EVENT_CLICK_PROFILE_FRIEND = "click_profile_friend"
        private const val EVENT_CLICK_PROFILE_MY_INFO = "click_profile_myInfo"
        private const val EVENT_CLICK_GO_SHOP = "click_go_shop"
        private const val NAME_SHOP_BUTTON = "shop_button"
        private const val VALUE_PROFILE_SHOP = "profile_shop"
        private const val EVENT_SCROLL_PROFILE_FRIENDS = "scroll_profile_friends"
        private const val EVENT_COMPLETE_PROFILE_DELETE_FRIEND = "complete_profile_delete_friend"
    }
}
