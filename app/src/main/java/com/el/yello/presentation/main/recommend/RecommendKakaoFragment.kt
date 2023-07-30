package com.el.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentRecommendKakaoBinding
import com.el.yello.util.context.yelloSnackbar
import com.example.domain.entity.RecommendModel
import com.example.ui.base.BindingFragment
import com.example.ui.intent.dpToPx
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecommendKakaoFragment :
    BindingFragment<FragmentRecommendKakaoBinding>(R.layout.fragment_recommend_kakao) {

    private val viewModel by viewModels<RecommendKakaoViewModel>()
    private var adapter: RecommendAdapter? = null
    private var recommendInviteDialog: RecommendInviteDialog? = null

    private lateinit var friendsList: List<RecommendModel.RecommendFriend>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInviteBtnListener()
        setKakaoRecommendList()
        setAdapterWithClickListener()
        observeAddListState()
        observeAddFriendState()
        setItemDivider()
        setDeleteAnimation()
    }

    override fun onDestroyView() {
        adapter = null
        dismissDialog()
        super.onDestroyView()
    }

    // 서버 통신 성공 시 카카오 추천 친구 추가
    private fun setKakaoRecommendList() {
        setListWithInfinityScroll()
        viewModel.initPagingVariable()
        viewModel.addListWithKakaoIdList()
    }

    // 무한 스크롤 구현
    private fun setListWithInfinityScroll() {
        binding.rvRecommendKakao.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvRecommendKakao.canScrollVertically(1) &&
                            layoutManager is LinearLayoutManager &&
                            layoutManager.findLastVisibleItemPosition() == adapter!!.itemCount - 1
                        ) {
                            viewModel.addListWithKakaoIdList()
                        }
                    }
                }
            }
        })
    }

    private fun initInviteBtnListener() {
        recommendInviteDialog = RecommendInviteDialog.newInstance(viewModel.getYelloId())
        binding.layoutInviteFriend.setOnSingleClickListener {
            recommendInviteDialog?.show(parentFragmentManager, DIALOG)
        }
        binding.btnRecommendNoFriend.setOnSingleClickListener {
            recommendInviteDialog?.show(parentFragmentManager, DIALOG)
        }
    }

    // 어댑터 클릭 리스너 설정
    private fun setAdapterWithClickListener() {
        adapter = RecommendAdapter { recommendModel, position, holder ->
            viewModel.setPositionAndHolder(position, holder)
            viewModel.addFriendToServer(recommendModel.id.toLong())
        }
        binding.rvRecommendKakao.adapter = adapter
    }

    // 추천친구 리스트 추가 서버 통신 성공 시 어댑터에 리스트 추가
    private fun observeAddListState() {
        viewModel.postState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data?.friends?.isEmpty() == true && adapter?.itemCount == 0) {
                        showNoFriendScreen()
                    } else {
                        showFriendListScreen()
                        friendsList = state.data?.friends ?: listOf()
                        adapter?.addItemList(friendsList)
                    }
                }

                is UiState.Failure -> {
                    showNoFriendScreen()
                    yelloSnackbar(
                        requireView(),
                        getString(R.string.recommend_error_friend_connection),
                    )
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    // 친구 추가 서버 통신 성공 시 리스트에서 아이템 삭제 & 서버 통신 중 액티비티 클릭 방지
    private fun observeAddFriendState() {
        viewModel.addState.observe(viewLifecycleOwner) { state ->
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

                is UiState.Empty -> {
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }
        }
    }

    private fun setItemDivider() {
        binding.rvRecommendKakao.addItemDecoration(
            RecommendItemDecoration(requireContext()),
        )
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
        if (recommendInviteDialog?.isAdded == true) recommendInviteDialog?.dismiss()
    }

    // 삭제 시 체크 버튼으로 전환 후 0.3초 뒤 애니메이션 적용
    private fun removeItemWithAnimation(holder: RecommendViewHolder, position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            changeToCheckIcon(holder)
            delay(300)
            adapter?.removeItem(position)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            delay(400)
            if (adapter?.itemCount == 0) {
                showNoFriendScreen()
            }
        }
    }

    private fun changeToCheckIcon(holder: RecommendViewHolder) {
        holder.binding.btnRecommendItemAdd.apply {
            text = null
            setIconResource(R.drawable.ic_check)
            setIconTintResource(R.color.black)
            iconPadding = dpToPx(holder.binding.root.context, -2)
            setPadding(dpToPx(holder.binding.root.context, 10))
        }
    }

    private fun showFriendListScreen() {
        binding.layoutRecommendFriendsList.isVisible = true
        binding.layoutRecommendNoFriendsList.isVisible = false
    }

    private fun showNoFriendScreen() {
        binding.layoutRecommendFriendsList.isVisible = false
        binding.layoutRecommendNoFriendsList.isVisible = true
    }

    private companion object {
        const val DIALOG = "dialog"
    }
}
