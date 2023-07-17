package com.yello.presentation.main.recommend

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.RecommendModel
import com.example.ui.base.BindingFragment
import com.example.ui.intent.dpToPx
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.yello.R
import com.yello.databinding.FragmentRecommendKakaoBinding
import com.yello.util.context.yelloSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RecommendKakaoFragment :
    BindingFragment<FragmentRecommendKakaoBinding>(R.layout.fragment_recommend_kakao) {

    private val viewModel by viewModels<RecommendKakaoViewModel>()
    private var adapter: RecommendAdapter? = null

    private var recommendInviteDialog: RecommendInviteDialog = RecommendInviteDialog()

    private lateinit var friendsList: List<RecommendModel>
    private lateinit var kakaoFriendIdList: List<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFriendIdList()
        initInviteButtonListener()
        initItemClickListener()
        observeAddListState()
        observeAddFriendState()
        setItemDivider()
        setDeleteAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissDialog()
    }

    private fun getFriendIdList() {
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Timber.e(error, "카카오 친구목록 가져오기 실패")

            } else if (friends != null) {
                val friendList: List<Friend>? = friends.elements
                kakaoFriendIdList = friendList?.map { friend -> friend.id.toString() } ?: listOf()

                initFirstList(kakaoFriendIdList)
                setListWithInfinityScroll(kakaoFriendIdList)

            } else {
                Timber.d("연동 가능한 카카오톡 친구 없음")
            }
        }
    }

    private fun initInviteButtonListener() {
        binding.layoutInviteFriend.setOnSingleClickListener {
            recommendInviteDialog.show(parentFragmentManager, "Dialog")
        }
        binding.btnRecommendNoFriend.setOnSingleClickListener {
            recommendInviteDialog.show(parentFragmentManager, "Dialog")
        }
    }

    private fun initFirstList(list: List<String>) {
        viewModel.addListFromServer(list)
    }

    private fun setListWithInfinityScroll(list: List<String>) {
        binding.rvRecommendKakao.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (!binding.rvRecommendKakao.canScrollVertically(1) &&
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == adapter!!.itemCount - 1
                    ) {
                        viewModel.addListFromServer(list)
                    }
                }
            }
        })
    }

    private fun observeAddListState() {
        viewModel.postState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data == listOf<RecommendModel>()) {
                        binding.layoutRecommendFriendsList.isVisible = false
                        binding.layoutRecommendNoFriendsList.isVisible = true
                    } else {
                        binding.layoutRecommendFriendsList.isVisible = true
                        friendsList = state.data
                        binding.rvRecommendKakao.adapter = adapter?.apply {
                            addItemList(friendsList)
                        }
                    }
                }

                is UiState.Failure -> {
                    binding.layoutRecommendFriendsList.isVisible = false
                    binding.layoutRecommendNoFriendsList.isVisible = true
                    yelloSnackbar(requireView(), state.msg)
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    private fun initItemClickListener() {
        adapter = RecommendAdapter { recommendModel, position, holder ->
            viewModel.setPositionAndHolder(position, holder)
            viewModel.addFriendToServer(recommendModel.id.toLong())
        }
    }

    private fun observeAddFriendState() {
        viewModel.addState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    val position = viewModel.itemPosition
                    val holder = viewModel.itemHolder
                    if (position != null && holder != null) {
                        removeItemWithAnimation(holder, position)
                    }
                    if (adapter?.itemCount == 0) {
                        binding.layoutRecommendFriendsList.isVisible = false
                        binding.layoutRecommendNoFriendsList.isVisible = true
                    }
                }

                is UiState.Failure -> {
                    yelloSnackbar(requireView(), state.msg)
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }

    private fun setItemDivider() {
        binding.rvRecommendKakao.addItemDecoration(
            RecommendItemDecoration(requireContext())
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
        if (recommendInviteDialog.isAdded) recommendInviteDialog.dismiss()
    }

    private fun removeItemWithAnimation(holder: RecommendViewHolder, position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            changeToCheckIcon(holder)
            delay(300)
            adapter?.removeItem(position)
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
}