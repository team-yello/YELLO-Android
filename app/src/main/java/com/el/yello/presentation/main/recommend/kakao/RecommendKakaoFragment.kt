package com.el.yello.presentation.main.recommend.kakao

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
import com.el.yello.databinding.FragmentRecommendKakaoBinding
import com.el.yello.presentation.main.dialog.InviteFriendDialog
import com.el.yello.presentation.main.recommend.list.RecommendAdapter
import com.el.yello.presentation.main.recommend.list.RecommendItemDecoration
import com.el.yello.presentation.main.recommend.list.RecommendViewHolder
import com.el.yello.presentation.util.BaseLinearRcvItemDeco
import com.el.yello.util.Utils.setPullToScrollColor
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.domain.entity.RecommendListModel
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class RecommendKakaoFragment :
    BindingFragment<FragmentRecommendKakaoBinding>(R.layout.fragment_recommend_kakao) {

    private var _adapter: RecommendAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private lateinit var viewModel: RecommendKakaoViewModel

    private var inviteYesFriendDialog: InviteFriendDialog? = null
    private var inviteNoFriendDialog: InviteFriendDialog? = null
    private var lastClickedRecommendModel: RecommendListModel.RecommendFriend? = null

    private var recommendKakaoBottomSheet: RecommendKakaoBottomSheet? = null

    private lateinit var itemDivider: RecommendItemDecoration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInviteBtnListener()
        initPullToScrollListener()
        setKakaoRecommendList()
        setAdapterWithClickListener()
        observeUserDataState()
        observeAddListState()
        observeAddFriendState()
        observeKakaoError()
        setItemDecoration()
        setInfinityScroll()
        setDeleteAnimation()
        AmplitudeUtils.trackEventWithProperties("view_recommend_kakao")
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isSearchViewShowed) {
            adapter.clearList()
            setKakaoRecommendList()
            viewModel.isSearchViewShowed = false
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[RecommendKakaoViewModel::class.java]
        viewModel.isSearchViewShowed = false
    }

    // 서버 통신 성공 시 카카오 추천 친구 추가
    private fun setKakaoRecommendList() {
        with(viewModel) {
            setFirstPageLoading()
            initViewModelVariable()
            addListWithKakaoIdList()
        }
    }

    // 무한 스크롤 구현
    private fun setInfinityScroll() {
        binding.rvRecommendKakao.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvRecommendKakao.canScrollVertically(1) && layoutManager is LinearLayoutManager && layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                            viewModel.addListWithKakaoIdList()
                        }
                    }
                }
            }
        })
    }

    private fun initInviteBtnListener() {
        binding.layoutInviteFriend.setOnSingleClickListener {
            inviteYesFriendDialog =
                InviteFriendDialog.newInstance(viewModel.getYelloId(), KAKAO_YES_FRIEND)
            AmplitudeUtils.trackEventWithProperties(
                "click_invite",
                JSONObject().put("invite_view", KAKAO_YES_FRIEND),
            )
            inviteYesFriendDialog?.show(parentFragmentManager, INVITE_DIALOG)
        }

        binding.btnRecommendNoFriend.setOnSingleClickListener {
            inviteNoFriendDialog =
                InviteFriendDialog.newInstance(viewModel.getYelloId(), KAKAO_NO_FRIEND)
            AmplitudeUtils.trackEventWithProperties(
                "click_invite",
                JSONObject().put("invite_view", KAKAO_NO_FRIEND),
            )
            inviteNoFriendDialog?.show(parentFragmentManager, INVITE_DIALOG)
        }
    }

    private fun initPullToScrollListener() {
        binding.layoutRecommendKakaoSwipe.apply {
            setOnRefreshListener {
                lifecycleScope.launch {
                    adapter.clearList()
                    setKakaoRecommendList()
                    delay(200)
                    binding.layoutRecommendKakaoSwipe.isRefreshing = false
                }
            }
            setPullToScrollColor(R.color.grayscales_500, R.color.grayscales_700)
        }
    }

    private fun setItemDecoration() {
        itemDivider = RecommendItemDecoration(requireContext())
        binding.rvRecommendKakao.addItemDecoration(itemDivider)
        binding.rvRecommendKakao.addItemDecoration(BaseLinearRcvItemDeco(bottomPadding = 12))
    }

    // 어댑터 클릭 리스너 설정
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
        binding.rvRecommendKakao.adapter = adapter
    }

    private fun observeKakaoError() {
        viewModel.getKakaoErrorResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (result) {
                yelloSnackbar(requireView(), getString(R.string.recommend_error_friends_list))
                showShimmerView(isLoading = true, hasList = true)
            }
        }.launchIn(lifecycleScope)
    }

    // 추천친구 리스트 추가 서버 통신 성공 시 어댑터에 리스트 추가
    private fun observeAddListState() {
        viewModel.postFriendsListState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    startFadeIn()
                    if (state.data.friends.isEmpty() && adapter.itemCount == 0) {
                        showShimmerView(isLoading = false, hasList = false)
                    } else {
                        showShimmerView(isLoading = false, hasList = true)
                        adapter.addItemList(state.data.friends)
                    }
                }

                is UiState.Failure -> {
                    showShimmerView(isLoading = true, hasList = true)
                    yelloSnackbar(
                        requireView(),
                        getString(R.string.recommend_error_friend_connection),
                    )
                }

                is UiState.Loading -> showShimmerView(isLoading = true, hasList = true)

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    // 친구 추가 서버 통신 성공 시 리스트에서 아이템 삭제 & 서버 통신 중 액티비티 클릭 방지
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
                        getString(R.string.recommend_error_add_friend_connection),
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
            when (state) {
                is UiState.Success -> {
                    viewModel.clickedUserData = state.data.apply {
                        if (!this.yelloId.startsWith("@")) this.yelloId = "@" + this.yelloId
                    }
                    if (lastClickedRecommendModel != null) {
                        recommendKakaoBottomSheet = RecommendKakaoBottomSheet()
                        recommendKakaoBottomSheet?.show(parentFragmentManager, ITEM_BOTTOM_SHEET)
                    }
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), getString(R.string.profile_error_user_data))
                }

                is UiState.Empty -> return@onEach

                is UiState.Loading -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun setDeleteAnimation() {
        binding.rvRecommendKakao.itemAnimator = object : DefaultItemAnimator() {
            override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
                holder.itemView.animation =
                    AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_out_right)
                return super.animateRemove(holder)
            }
        }
    }

    // 삭제 시 체크 버튼으로 전환 후 0.3초 뒤 애니메이션 적용
    private fun removeItemWithAnimation(holder: RecommendViewHolder, position: Int) {
        lifecycleScope.launch {
            changeToCheckIcon(holder)
            delay(300)
            binding.rvRecommendKakao.removeItemDecoration(itemDivider)
            adapter.removeItem(position)
            delay(500)
            binding.rvRecommendKakao.addItemDecoration(itemDivider)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            if (adapter.itemCount == 0) {
                showShimmerView(isLoading = false, hasList = false)
            }
        }
    }

    private fun changeToCheckIcon(holder: RecommendViewHolder) {
        with(holder.binding) {
            btnRecommendItemAdd.isVisible = false
            btnRecommendItemAddPressed.isVisible = true
        }
    }

    private fun startFadeIn() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        binding.rvRecommendKakao.startAnimation(animation)
    }

    private fun showShimmerView(isLoading: Boolean, hasList: Boolean) {
        with(binding) {
            if (isLoading) shimmerFriendList.startShimmer() else shimmerFriendList.stopShimmer()
            layoutRecommendFriendsList.isVisible = hasList
            layoutRecommendNoFriendsList.isVisible = !hasList
            shimmerFriendList.isVisible = isLoading
            rvRecommendKakao.isVisible = !isLoading
        }
    }

    fun scrollToTop() {
        binding.rvRecommendKakao.smoothScrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
        if (inviteYesFriendDialog?.isAdded == true) inviteYesFriendDialog?.dismiss()
        if (inviteNoFriendDialog?.isAdded == true) inviteNoFriendDialog?.dismiss()
        if (recommendKakaoBottomSheet != null) recommendKakaoBottomSheet?.dismiss()
    }

    private companion object {
        const val INVITE_DIALOG = "inviteDialog"
        const val KAKAO_NO_FRIEND = "recommend_kakao_nofriend"
        const val KAKAO_YES_FRIEND = "recommend_kakao_yesfriend"
        const val ITEM_BOTTOM_SHEET = "itemBottomSheet"
    }
}
