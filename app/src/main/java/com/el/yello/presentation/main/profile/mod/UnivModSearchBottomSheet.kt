package com.el.yello.presentation.main.profile.mod

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentModSearchBottomSheetBinding
import com.el.yello.presentation.main.profile.mod.UnivProfileModViewModel.Companion.TEXT_NONE
import com.el.yello.presentation.onboarding.fragment.universityinfo.department.DepartmentAdapter
import com.el.yello.presentation.onboarding.fragment.universityinfo.department.SearchDialogDepartmentFragment.Companion.DEPARTMENT_FORM_URL
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

class UnivModSearchBottomSheet :
    BindingBottomSheetDialog<FragmentModSearchBottomSheetBinding>(R.layout.fragment_mod_search_bottom_sheet) {

    private val viewModel by activityViewModels<UnivProfileModViewModel>()

    private var _univAdapter: UniversityAdapter? = null
    private val univAdapter
        get() = requireNotNull(_univAdapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private var _groupAdapter: DepartmentAdapter? = null
    private val groupAdapter
        get() = requireNotNull(_groupAdapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private var searchJob: Job? = null
    private var searchText: String = ""

    private var isUnivSearch = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIsUnivSearch()
        initAdapter()
        initSchoolFormBtnListener()
        initBackBtnListener()
        setDebounceSearch()
        setListWithInfinityScroll()
        setEmptyListWithTyping()
        observeGetUnivListState()
        observeGetUnivGroupIdListState()
        setHideKeyboard()
    }

    private fun checkIsUnivSearch() {
        isUnivSearch = arguments?.getBoolean(ARGS_IS_UNIV_SEARCH) ?: true
    }

    private fun initAdapter() {
        if (isUnivSearch) {
            binding.tvSearchTitle.text = getString(R.string.onboarding_tv_search_school)
            _univAdapter = UniversityAdapter(storeUniversity = ::storeUniversity)
            binding.rvSearchList.adapter = univAdapter
        } else {
            binding.tvSearchTitle.text = getString(R.string.onboarding_tv_search_department)
            _groupAdapter = DepartmentAdapter(storeDepartment = ::storeDepartment)
            binding.rvSearchList.adapter = groupAdapter
        }
    }

    private fun storeUniversity(school: String) {
        if (viewModel.school.value != school) {
            viewModel.school.value = school
            viewModel.subGroup.value = TEXT_NONE
            viewModel.isChanged = true
        }
        dismiss()
    }

    private fun storeDepartment(department: String, groupId: Long) {
        if (viewModel.groupId != groupId) {
            viewModel.groupId = groupId
            viewModel.subGroup.value = department
            viewModel.isChanged = true
            (activity as UnivProfileModActivity).checkIsTextNone()
        }
        dismiss()
    }

    private fun initSchoolFormBtnListener() {
        if (isUnivSearch) {
            setFormBtn(getString(R.string.onboarding_tv_add_school), SCHOOL_FORM_URL)
        } else {
            setFormBtn(getString(R.string.onboarding_btn_add_department), DEPARTMENT_FORM_URL)
        }
    }

    private fun setFormBtn(btnText: String, uri: String) {
        with(binding.btnAddForm) {
            text = btnText
            setOnSingleClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
                    startActivity(this)
                }
            }
        }
    }

    private fun initBackBtnListener() {
        binding.btnBackDialog.setOnSingleClickListener { dismiss() }
    }

    private fun setDebounceSearch() {
        binding.etSearch.doAfterTextChanged { input ->
            searchJob?.cancel()
            searchJob = viewModel.viewModelScope.launch {
                delay(debounceTime)
                input.toString().let { text ->
                    searchText = text
                    getSearchListFromServer(searchText)
                }
            }
        }
    }

    private fun setListWithInfinityScroll() {
        binding.rvSearchList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvSearchList.canScrollVertically(1) && layoutManager is LinearLayoutManager && layoutManager.findLastVisibleItemPosition() == univAdapter.itemCount - 1) {
                            getSearchListFromServer(searchText)
                        }
                    }
                }
            }
        })
    }

    private fun getSearchListFromServer(searchText: String) {
        if (isUnivSearch) {
            viewModel.getUnivListFromServer(searchText)
        } else {
            viewModel.getUnivGroupIdListFromServer(searchText)
        }
    }

    private fun setEmptyListWithTyping() {
        binding.etSearch.doOnTextChanged { _, _, _, _ ->
            lifecycleScope.launch {
                viewModel.setNewPage()
                if (isUnivSearch) {
                    univAdapter.submitList(listOf())
                    univAdapter.notifyDataSetChanged()
                } else {
                    groupAdapter.submitList(listOf())
                    groupAdapter.notifyDataSetChanged()
                }
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

    private fun observeGetUnivListState() {
        viewModel.getUnivListState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> univAdapter.submitList(state.data.schoolList)

                is UiState.Failure -> toast(getString(R.string.recommend_search_error))

                is UiState.Loading -> return@onEach

                is UiState.Empty -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeGetUnivGroupIdListState() {
        viewModel.getUnivGroupIdListState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> groupAdapter.submitList(state.data.groupList)

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
        binding.layoutModSearchBottomSheet.setOnSingleClickListener {
            requireContext().hideKeyboard(requireView())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _groupAdapter = null
        _univAdapter = null
        searchJob?.cancel()
        viewModel.resetStateVariables()
    }

    companion object {
        @JvmStatic
        fun newInstance(isUnivSearch: Boolean) = UnivModSearchBottomSheet().apply {
            arguments = bundleOf(ARGS_IS_UNIV_SEARCH to isUnivSearch)
        }

        private const val ARGS_IS_UNIV_SEARCH = "IS_UNIV_SEARCH"

        const val debounceTime = 500L
    }
}