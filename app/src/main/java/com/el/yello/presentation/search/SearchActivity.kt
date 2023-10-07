package com.el.yello.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ActivitySearchBinding
import com.el.yello.util.Utils.setPullToScrollColor
import com.el.yello.util.amplitude.AmplitudeUtils
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : BindingActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private var _adapter: SearchPageAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private val viewModel by viewModels<SearchViewModel>()

    private val debounceTime = 500L
    private var searchJob: Job? = null

    private var searchText: String = ""

    private var isNoFriend: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapter()
        initFocusToEditText()
        initBackBtnListener()
        setPullToScrollListener()
        setLoadingScreen()
        setDebounceSearch()
        observeSearchPagingList(searchText)
        observePagingLoadingState()
        observeAddFriendState()
        setListWithInfinityScroll()
        AmplitudeUtils.trackEventWithProperties("click_search_addfriend")
    }

    private fun initAdapter() {
        _adapter = SearchPageAdapter { searchFriendModel, position, holder ->
            viewModel.setPositionAndHolder(position, holder)
            viewModel.addFriendToServer(searchFriendModel.id.toLong())
        }
        adapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.prepend.endOfPaginationReached) {
                binding.layoutRecommendNoSearch.isVisible = adapter.itemCount < 1
                binding.rvRecommendSearch.isGone = adapter.itemCount < 1
                isNoFriend = adapter.itemCount < 1
            }
        }
        binding.rvRecommendSearch.adapter = adapter
        binding.lifecycleOwner = this
        binding.rvRecommendSearch.addItemDecoration(SearchItemDecoration(this))
    }

    // 처음 들어왔을 때 키보드 올라오도록 설정 (개인정보보호옵션 켜진 경우 불가능)
    private fun initFocusToEditText() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.etRecommendSearchBox.requestFocus()
        inputMethodManager.showSoftInput(
            binding.etRecommendSearchBox, InputMethodManager.SHOW_IMPLICIT
        )
    }

    private fun initBackBtnListener() {
        binding.btnRecommendSearchBack.setOnSingleClickListener { finish() }
    }

    private fun setPullToScrollListener() {
        binding.layoutSearchSwipe.apply {
            setOnRefreshListener {
                adapter.refresh()
                viewModel.setFirstLoading(true)
            }
            setPullToScrollColor(R.color.grayscales_500, R.color.grayscales_700)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                adapter.loadStateFlow.distinctUntilChangedBy { it.refresh }.collect {
                    delay(200)
                    binding.layoutSearchSwipe.isRefreshing = false
                }
            }
        }
    }

    // 텍스트 변경 감지 시 로딩 화면 출력
    private fun setLoadingScreen() {
        binding.etRecommendSearchBox.doOnTextChanged { _, _, _, _ ->
            lifecycleScope.launch {
                showLoadingScreen()
                adapter.submitData(PagingData.empty())
            }
        }
    }

    // 0.5초 뒤 검색 서버통신 진행
    private fun setDebounceSearch() {
        binding.etRecommendSearchBox.doAfterTextChanged { text ->
            searchJob?.cancel()
            if (text.isNullOrBlank()) {
                showNoFriendScreen()
                binding.layoutRecommendNoSearch.visibility = View.GONE
            } else {
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

    private fun observeSearchPagingList(keyword: String) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.setListFromServer(keyword).collectLatest { adapter.submitData(it) }
            }
        }
    }

    // 검색 리스트 추가 서버 통신 성공 시 어댑터에 리스트 추가
    private fun observePagingLoadingState() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        if (!isNoFriend) showLoadingScreen()
                    }

                    is LoadState.NotLoading -> {
                        if (viewModel.isFirstLoading.value) {
                            startFadeIn()
                            viewModel.setFirstLoading(false)
                            showFriendListScreen()
                        } else {
                            showFriendListScreen()
                        }
                    }

                    is LoadState.Error -> {
                        showNoFriendScreen()
                        toast(getString(R.string.recommend_search_error))
                    }
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
                        if (!binding.rvRecommendSearch.canScrollVertically(1) && layoutManager is LinearLayoutManager && layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                            viewModel.setListFromServer(searchText)
                        }
                    }
                }
            }
        })
    }

    // 친구 추가 서버 통신 성공 시 표시 변경
    private fun observeAddFriendState() {
        lifecycleScope.launch {
            viewModel.addFriendState.collectLatest { state ->
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

    private fun startFadeIn() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.rvRecommendSearch.startAnimation(animation)
    }

    private fun showFriendListScreen() {
        binding.layoutSearchSwipe.visibility = View.VISIBLE
        binding.layoutRecommendSearchLoading.visibility = View.GONE
        binding.layoutRecommendNoSearch.visibility = View.GONE
    }

    private fun showLoadingScreen() {
        binding.layoutSearchSwipe.visibility = View.GONE
        binding.layoutRecommendSearchLoading.visibility = View.VISIBLE
        binding.layoutRecommendNoSearch.visibility = View.GONE
    }

    private fun showNoFriendScreen() {
        binding.layoutSearchSwipe.visibility = View.GONE
        binding.layoutRecommendSearchLoading.visibility = View.GONE
        binding.layoutRecommendNoSearch.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
    }
}