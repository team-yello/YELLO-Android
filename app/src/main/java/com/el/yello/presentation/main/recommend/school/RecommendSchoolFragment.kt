package com.el.yello.presentation.main.recommend.school

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentRecommendSchoolBinding
import com.el.yello.presentation.main.dialog.invite.InviteFriendDialog
import com.el.yello.presentation.main.recommend.list.RecommendAdapter
import com.el.yello.presentation.main.recommend.list.RecommendItemDecoration
import com.el.yello.presentation.main.recommend.list.RecommendViewHolder
import com.el.yello.presentation.util.BaseLinearRcvItemDeco
import com.el.yello.util.manager.AmplitudeManager
import com.el.yello.util.extension.yelloSnackbar
import com.example.domain.entity.RecommendListModel.RecommendFriend
import com.example.ui.base.BindingFragment
import com.example.ui.state.UiState
import com.example.ui.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class RecommendSchoolFragment :
    BindingFragment<FragmentRecommendSchoolBinding>(R.layout.fragment_recommend_school) {

    private var _adapter: RecommendAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private lateinit var viewModel: RecommendSchoolViewModel

    private var inviteYesFriendDialog: InviteFriendDialog? = null
    private var inviteNoFriendDialog: InviteFriendDialog? = null
    private var lastClickedRecommendModel: RecommendFriend? = null

    private var recommendSchoolBottomSheet: RecommendSchoolBottomSheet? = null

    private lateinit var friendsList: List<RecommendFriend>

    private lateinit var itemDivider: RecommendItemDecoration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initFirstList()
        initInviteBtnListener()
        initPullToScrollListener()
        setItemDecoration()
        setAdapterWithClickListener()
        setListWithInfinityScroll()
        observeAddFriendsListState()
        observeAddFriendState()
        observeUserDataState()
        setDeleteAnimation()
        AmplitudeManager.trackEventWithProperties("view_recommend_school")
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isSearchViewShowed) {
            adapter.clearList()
            viewModel.setFirstPageLoading()
            viewModel.getSchoolFriendsListFromServer()
            viewModel.isSearchViewShowed = false
        }
    }

    private fun initInviteBtnListener() {
        binding.layoutInviteFriend.setOnSingleClickListener {
            inviteYesFriendDialog =
                InviteFriendDialog.newInstance(viewModel.getYelloId(), SCHOOL_YES_FRIEND)
            AmplitudeManager.trackEventWithProperties(
                "click_invite",
                JSONObject().put("invite_view", SCHOOL_YES_FRIEND),
            )
            inviteYesFriendDialog?.show(parentFragmentManager, INVITE_DIALOG)
        }

        binding.btnRecommendNoFriend.setOnSingleClickListener {
            inviteNoFriendDialog =
                InviteFriendDialog.newInstance(viewModel.getYelloId(), SCHOOL_NO_FRIEND)
            AmplitudeManager.trackEventWithProperties(
                "click_invite",
                JSONObject().put("invite_view", SCHOOL_NO_FRIEND),
            )
            inviteNoFriendDialog?.show(parentFragmentManager, INVITE_DIALOG)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[RecommendSchoolViewModel::class.java]
        viewModel.isSearchViewShowed = false
    }

    private fun initFirstList() {
        viewModel.setFirstPageLoading()
        viewModel.getSchoolFriendsListFromServer()
    }

    private fun initPullToScrollListener() {
        binding.layoutRecommendSchoolSwipe.apply {
            setOnRefreshListener {
                lifecycleScope.launch {
                    adapter.clearList()
                    viewModel.setFirstPageLoading()
                    viewModel.getSchoolFriendsListFromServer()
                    delay(200)
                    binding.layoutRecommendSchoolSwipe.isRefreshing = false
                }
            }
            setPullToScrollColor(R.color.grayscales_500, R.color.grayscales_700)
        }
    }

    private fun setItemDecoration() {
        itemDivider = RecommendItemDecoration(requireContext())
        binding.rvRecommendSchool.addItemDecoration(itemDivider)
        binding.rvRecommendSchool.addItemDecoration(BaseLinearRcvItemDeco(bottomPadding = 12))
    }

    private fun setAdapterWithClickListener() {
        _adapter = RecommendAdapter(
            buttonClick = { recommendModel, position, holder ->
                viewModel.setPositionAndHolder(position, holder)
                viewModel.addFriendToServer(recommendModel.id.toLong())
            },
            itemClick = { recommendModel, position, holder ->
                viewModel.setPositionAndHolder(position, holder)
                viewModel.getUserDataFromServer(recommendModel.id.toLong())
                lastClickedRecommendModel = recommendModel
            },
        )
        binding.rvRecommendSchool.adapter = adapter
    }

    private fun setListWithInfinityScroll() {
        binding.rvRecommendSchool.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvRecommendSchool.canScrollVertically(1) && layoutManager is LinearLayoutManager && layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                            viewModel.getSchoolFriendsListFromServer()
                        }
                    }
                }
            }
        })
    }

    private fun observeAddFriendsListState() {
        viewModel.postFriendsListState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    startFadeIn()
                    if (state.data.friends.isEmpty() && adapter.itemCount == 0) {
                        showShimmerView(isLoading = false, hasList = false)
                    } else {
                        showShimmerView(isLoading = false, hasList = true)
                        friendsList = state.data.friends
                        adapter.addItemList(friendsList)
                    }
                }

                is UiState.Failure -> {
                    showShimmerView(isLoading = true, hasList = true)
                    yelloSnackbar(
                        requireView(),
                        getString(R.string.internet_connection_error_msg),
                    )
                }

                is UiState.Loading -> showShimmerView(isLoading = true, hasList = true)

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeAddFriendState() {
        viewModel.addFriendState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    val position = viewModel.itemPosition
                    val holder = viewModel.itemHolder
                    if (position != null && holder != null) {
                        removeItemWithAnimation(holder, position)
                    } else {
                        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }
                }

                is UiState.Failure -> {
                    yelloSnackbar(
                        requireView(),
                        getString(R.string.internet_connection_error_msg),
                    )
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                is UiState.Loading -> {
                    activity?.window?.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    )
                }

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeUserDataState() {
        viewModel.getUserDataState.flowWithLifecycle(lifecycle).onEach { state ->
            if (state is UiState.Success) {
                viewModel.clickedUserData = state.data.apply {
                    if (!this.yelloId.startsWith("@")) this.yelloId = "@" + this.yelloId
                }
                if (lastClickedRecommendModel != null) {
                    recommendSchoolBottomSheet = RecommendSchoolBottomSheet()
                    recommendSchoolBottomSheet?.show(parentFragmentManager, ITEM_BOTTOM_SHEET)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun setDeleteAnimation() {
        binding.rvRecommendSchool.itemAnimator = object : DefaultItemAnimator() {
            override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
                holder.itemView.animation =
                    AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_out_right)
                return super.animateRemove(holder)
            }
        }
    }

    private fun removeItemWithAnimation(holder: RecommendViewHolder, position: Int) {
        lifecycleScope.launch {
            changeToCheckIcon(holder)
            delay(300)
            binding.rvRecommendSchool.removeItemDecoration(itemDivider)
            adapter.removeItem(position)
            delay(500)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            binding.rvRecommendSchool.addItemDecoration(itemDivider)
            if (adapter.itemCount == 0) {
                showShimmerView(isLoading = false, hasList = false)
            }
        }
    }

    private fun changeToCheckIcon(holder: RecommendViewHolder) {
        with(holder.binding) {
            btnRecommendItemAdd.visibility = View.INVISIBLE
            btnRecommendItemAddPressed.visibility = View.VISIBLE
        }
    }

    private fun startFadeIn() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        binding.rvRecommendSchool.startAnimation(animation)
    }

    private fun showShimmerView(isLoading: Boolean, hasList: Boolean) {
        with(binding) {
            if (isLoading) shimmerFriendList.startShimmer() else shimmerFriendList.stopShimmer()
            layoutRecommendFriendsList.isVisible = hasList
            layoutRecommendNoFriendsList.isVisible = !hasList
            shimmerFriendList.isVisible = isLoading
            rvRecommendSchool.isVisible = !isLoading
        }
    }

    fun scrollToTop() {
        binding.rvRecommendSchool.smoothScrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
        if (inviteYesFriendDialog?.isAdded == true) inviteYesFriendDialog?.dismiss()
        if (inviteNoFriendDialog?.isAdded == true) inviteNoFriendDialog?.dismiss()
        if (recommendSchoolBottomSheet != null) recommendSchoolBottomSheet?.dismiss()
    }

    private companion object {
        const val INVITE_DIALOG = "inviteDialog"
        const val SCHOOL_NO_FRIEND = "recommend_school_nofriend"
        const val SCHOOL_YES_FRIEND = "recommend_school_yesfriend"
        const val ITEM_BOTTOM_SHEET = "itemBottomSheet"
    }
}
