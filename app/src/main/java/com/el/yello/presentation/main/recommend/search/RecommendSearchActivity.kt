package com.el.yello.presentation.main.recommend.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityRecommendSearchBinding
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendSearchActivity :
    BindingActivity<ActivityRecommendSearchBinding>(R.layout.activity_recommend_search) {

    private var _adapter: RecommendSearchAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private val viewModel by viewModels<RecommendSearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFocusToEditText()
        initAdapterWithDivider()
        initBackBtnListener()
        observeAddFriendState()
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
    }

    private fun initAdapterWithDivider() {
        _adapter = RecommendSearchAdapter { searchFriendModel, position, holder ->
            viewModel.setPositionAndHolder(position, holder)
            viewModel.addFriendToServer(searchFriendModel.id.toLong())
        }
        binding.rvRecommendSearch.adapter = adapter
        binding.rvRecommendSearch.addItemDecoration(
            RecommendSearchItemDecoration(this)
        )
    }

    private fun initFocusToEditText() {
        binding.etRecommendSearchBox.requestFocus()
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            binding.etRecommendSearchBox,
            InputMethodManager.SHOW_IMPLICIT
        )
    }

    private fun initBackBtnListener() {
        binding.btnRecommendSearchBack.setOnSingleClickListener {
            finish()
        }
    }

    // 친구 추가 서버 통신 성공 시 표시 변경
    private fun observeAddFriendState() {
        viewModel.addFriendState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    val position = viewModel.itemPosition
                    val holder = viewModel.itemHolder
                    if (position != null && holder != null) {
                        holder.binding.btnRecommendItemAdd.visibility = View.GONE
                        holder.binding.btnRecommendItemMyFriend.visibility = View.VISIBLE
                    }
                }

                is UiState.Failure -> {
                    yelloSnackbar(
                        binding.root.rootView,
                        getString(R.string.recommend_error_add_friend_connection),
                    )
                }

                is UiState.Loading -> {}

                is UiState.Empty -> {}
            }
        }
    }
}