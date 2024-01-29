package com.el.yello.presentation.main.profile.mod

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentUnivModSchoolBottomSheetBinding
import com.el.yello.presentation.onboarding.fragment.universityinfo.university.SearchDialogUniversityFragment.Companion.SCHOOL_FORM_URL
import com.el.yello.presentation.onboarding.fragment.universityinfo.university.UniversityAdapter
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.context.hideKeyboard
import com.example.ui.fragment.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UnivModSchoolBottomSheet :
    BindingBottomSheetDialog<FragmentUnivModSchoolBottomSheetBinding>(R.layout.fragment_univ_mod_school_bottom_sheet) {

    private val viewModel by activityViewModels<UnivProfileModViewModel>()

    private var _adapter: UniversityAdapter? = null
    private val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private var searchJob: Job? = null
    private var searchText: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initSchoolFormBtnListener()
        initBackBtnListener()
        setDebounceSearch()
        setListWithInfinityScroll()
        setEmptyListWithTyping()
        observeSearchListState()
        setHideKeyboard()
    }

    private fun initAdapter() {
        _adapter = UniversityAdapter(storeUniversity = ::storeUniversity)
        binding.rvSchoolList.adapter = adapter
    }

    private fun storeUniversity(school: String) {
        viewModel.school.value = school
        dismiss()
    }

    private fun initSchoolFormBtnListener() {
        binding.btnSchoolAdd.setOnSingleClickListener {
            Intent(Intent.ACTION_VIEW, Uri.parse(SCHOOL_FORM_URL)).apply {
                startActivity(this)
            }
        }
    }

    private fun initBackBtnListener() {
        binding.btnBackDialog.setOnSingleClickListener { dismiss() }
    }

    private fun setDebounceSearch() {
        binding.etSchoolSearch.doAfterTextChanged { input ->
            searchJob?.cancel()
            searchJob = viewModel.viewModelScope.launch {
                delay(debounceTime)
                input.toString().let { text ->
                    searchText = text
                    viewModel.getUniversityListFromServer(text)
                }
            }
        }
    }

    private fun setListWithInfinityScroll() {
        binding.rvSchoolList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvSchoolList.canScrollVertically(1) && layoutManager is LinearLayoutManager && layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                            viewModel.getUniversityListFromServer(searchText)
                        }
                    }
                }
            }
        })
    }

    private fun setEmptyListWithTyping() {
        binding.etSchoolSearch.doOnTextChanged { _, _, _, _ ->
            lifecycleScope.launch {
                viewModel.setNewPage()
                adapter.submitList(listOf())
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { view ->
                val behaviour = BottomSheetBehavior.from(view)
                setupFullHeight(view)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun observeSearchListState() {
        viewModel.postUniversityListState.flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Success -> adapter.submitList(state.data.schoolList)

                    is UiState.Failure -> toast(getString(R.string.recommend_search_error))

                    is UiState.Loading -> return@onEach

                    is UiState.Empty -> return@onEach
                }
            }.launchIn(lifecycleScope)
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    private fun setHideKeyboard() {
        binding.layoutModSchoolBottomSheet.setOnSingleClickListener {
            requireContext().hideKeyboard(
                requireView(),
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
        searchJob?.cancel()
    }

    companion object {
        const val debounceTime = 500L
    }
}