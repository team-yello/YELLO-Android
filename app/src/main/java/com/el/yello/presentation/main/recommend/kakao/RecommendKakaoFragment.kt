package com.el.yello.presentation.main.recommend.kakao

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
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
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
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

    private lateinit var itemDivider: RecommendItemDecoration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInviteBtnListener()
        initPullToScrollListener()
        setKakaoRecommendList()
        setAdapterWithClickListener()
        observeKakaoError()
        observeAddListState()
        observeAddFriendState()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
        dismissDialog()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[RecommendKakaoViewModel::class.java]
        viewModel.isSearchViewShowed = false
    }

    // 서버 통신 성공 시 카카오 추천 친구 추가
    private fun setKakaoRecommendList() {
        viewModel.setFirstPageLoading()
        viewModel.initPagingVariable()
        viewModel.addListWithKakaoIdList()
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
        binding.rvRecommendKakao.addItemDecoration(
            BaseLinearRcvItemDeco(0, 0, 0, 0, 0, RecyclerView.VERTICAL, 12),
        )
    }

    // 어댑터 클릭 리스너 설정
    private fun setAdapterWithClickListener() {
        _adapter = RecommendAdapter { recommendModel, position, holder ->
            viewModel.setPositionAndHolder(position, holder)
            viewModel.addFriendToServer(recommendModel.id.toLong())
        }
        binding.rvRecommendKakao.adapter = adapter
    }

    private fun observeKakaoError() {
        lifecycleScope.launch {
            viewModel.getKakaoErrorResult.collectLatest { result ->
                if (result) {
                    yelloSnackbar(requireView(), getString(R.string.recommend_error_friends_list))
                    showShimmerScreen()
                }
            }
        }
    }

    // 추천친구 리스트 추가 서버 통신 성공 시 어댑터에 리스트 추가
    private fun observeAddListState() {
        lifecycleScope.launch {
            viewModel.postFriendsListState.collectLatest { state ->
                when (state) {
                    is UiState.Success -> {
                        startFadeIn()
                        if (state.data?.friends?.isEmpty() == true && adapter.itemCount == 0) {
                            showNoFriendScreen()
                        } else {
                            showFriendListScreen()
                            adapter.addItemList(state.data?.friends ?: listOf())
                        }
                    }

                    is UiState.Failure -> {
                        showShimmerScreen()
                        yelloSnackbar(
                            requireView(),
                            getString(R.string.recommend_error_friend_connection),
                        )
                    }

                    is UiState.Loading -> {
                        showShimmerScreen()
                    }

                    is UiState.Empty -> {}
                }
            }
        }
    }

    // 친구 추가 서버 통신 성공 시 리스트에서 아이템 삭제 & 서버 통신 중 액티비티 클릭 방지
    private fun observeAddFriendState() {
        lifecycleScope.launch {
            viewModel.addFriendState.collectLatest { state ->
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

                    is UiState.Empty -> {}
                }
            }
        }
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

    private fun dismissDialog() {
        if (inviteYesFriendDialog?.isAdded == true) inviteYesFriendDialog?.dismiss()
        if (inviteNoFriendDialog?.isAdded == true) inviteNoFriendDialog?.dismiss()
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
                showNoFriendScreen()
            }
        }
    }

    private fun changeToCheckIcon(holder: RecommendViewHolder) {
        holder.binding.btnRecommendItemAdd.visibility = View.INVISIBLE
        holder.binding.btnRecommendItemAddPressed.visibility = View.VISIBLE
    }

    private fun startFadeIn() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        binding.rvRecommendKakao.startAnimation(animation)
    }

    private fun showShimmerScreen() {
        binding.layoutRecommendFriendsList.isVisible = true
        binding.layoutRecommendNoFriendsList.isVisible = false
        binding.shimmerFriendList.startShimmer()
        binding.shimmerFriendList.visibility = View.VISIBLE
        binding.rvRecommendKakao.visibility = View.GONE
    }

    private fun showFriendListScreen() {
        binding.layoutRecommendFriendsList.isVisible = true
        binding.layoutRecommendNoFriendsList.isVisible = false
        binding.shimmerFriendList.stopShimmer()
        binding.shimmerFriendList.visibility = View.GONE
        binding.rvRecommendKakao.visibility = View.VISIBLE
    }

    private fun showNoFriendScreen() {
        binding.layoutRecommendFriendsList.isVisible = false
        binding.layoutRecommendNoFriendsList.isVisible = true
        binding.shimmerFriendList.stopShimmer()
    }

    fun scrollToTop() {
        binding.rvRecommendKakao.smoothScrollToPosition(0)
    }

    private companion object {
        const val INVITE_DIALOG = "inviteDialog"

        const val KAKAO_NO_FRIEND = "recommend_kakao_nofriend"
        const val KAKAO_YES_FRIEND = "recommend_kakao_yesfriend"
    }
}
