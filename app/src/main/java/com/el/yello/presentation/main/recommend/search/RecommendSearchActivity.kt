package com.el.yello.presentation.main.recommend.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ActivityRecommendSearchBinding
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecommendSearchActivity :
    BindingActivity<ActivityRecommendSearchBinding>(R.layout.activity_recommend_search) {

    private var _adapter: RecommendSearchAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private val viewModel by viewModels<RecommendSearchViewModel>()

    private val debounceTime = 500L
    private var searchJob: Job? = null

    private var searchText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFocusToEditText()
        initAdapterWithDivider()
        initBackBtnListener()
        observeSearchListState()
        observeAddFriendState()
        setDebounceSearch()
        setLoadingScreen()
        setListWithInfinityScroll()
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
    }

    private fun initAdapterWithDivider() {
        _adapter = RecommendSearchAdapter { searchFriendModel, position, holder ->
            viewModel.setPositionAndHolder(position, holder)
            viewModel.addFriendToServer(searchFriendModel.id.toLong())
            AmplitudeUtils.trackEventWithProperties("click_search_addfriend")
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

    private fun setLoadingScreen() {
        binding.etRecommendSearchBox.doOnTextChanged { _, _, _, _ ->
            showLoadingScreen()
            adapter.submitList(listOf())
            viewModel.setNewPage()
        }
    }

    private fun setDebounceSearch() {
        binding.etRecommendSearchBox.doAfterTextChanged { text ->
            if (text.isNullOrBlank()) {
                showNoFriendScreen()
                binding.layoutRecommendNoSearch.visibility = View.GONE
            } else {
                searchJob?.cancel()
                searchJob = viewModel.viewModelScope.launch {
                    delay(debounceTime)
                    text.toString().let { text ->
                        searchText = text
                        viewModel.setListFromServer(text)
                    }
                }
            }
        }
    }

    // 검색 리스트 추가 서버 통신 성공 시 어댑터에 리스트 추가
    private fun observeSearchListState() {
        viewModel.postFriendsListState.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data?.friendList?.size == 0) {
                        showNoFriendScreen()
                    } else {
                        adapter.addList(state.data?.friendList ?: listOf())
                        showFriendListScreen()
                    }
                }

                is UiState.Failure -> {
                    toast(getString(R.string.recommend_search_error))
                    showFriendListScreen()
                }

                is UiState.Loading -> {
                    showLoadingScreen()
                }

                is UiState.Empty -> {
                    showFriendListScreen()
                }
            }
        }
    }

    // 무한 스크롤 구현
    private fun setListWithInfinityScroll() {
        binding.rvRecommendSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvRecommendSearch.canScrollVertically(1)
                            && layoutManager is LinearLayoutManager
                            && layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1
                        ) {
                            viewModel.setListFromServer(searchText)
                        }
                    }
                }
            }
        })
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

    private fun showFriendListScreen() {
        binding.rvRecommendSearch.visibility = View.VISIBLE
        binding.layoutRecommendSearchLoading.visibility = View.GONE
        binding.layoutRecommendNoSearch.visibility = View.GONE
    }

    private fun showLoadingScreen() {
        binding.rvRecommendSearch.visibility = View.GONE
        binding.layoutRecommendSearchLoading.visibility = View.VISIBLE
        binding.layoutRecommendNoSearch.visibility = View.GONE
    }

    private fun showNoFriendScreen() {
        binding.rvRecommendSearch.visibility = View.GONE
        binding.layoutRecommendSearchLoading.visibility = View.GONE
        binding.layoutRecommendNoSearch.visibility = View.VISIBLE
    }
}