package com.el.yello.presentation.onboarding.fragment.universityinfo.department

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentDialogDepartmentBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.context.hideKeyboard
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchDialogDepartmentFragment :
    BindingBottomSheetDialog<FragmentDialogDepartmentBinding>(R.layout.fragment_dialog_department) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()
    private var adapter: DepartmentAdapter? = null
    private var inputText: String = ""
    private val debounceTime = 500L
    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initDepartmentDialogView()
        setupDepartmentData()
        recyclerviewScroll()
        setClickToDepartmentForm()
        setListWithInfinityScroll()
    }

    private fun initDepartmentDialogView() {
        setHideKeyboard()
        binding.etDepartmentSearch.doAfterTextChanged { it ->
            searchJob?.cancel()
            searchJob = viewModel.viewModelScope.launch {
                delay(debounceTime)
                it?.toString()?.let { viewModel.getGroupList(it) }
            }
        }
        adapter = DepartmentAdapter(storeDepartment = ::storeGroup)
        binding.rvDepartmentList.adapter = adapter
        binding.btnBackDialog.setOnSingleClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupDepartmentData() {
        viewModel.departmentData.observe(viewLifecycleOwner) { state ->
            Timber.d("GET GROUP LIST OBSERVE : $state")
            when (state) {
                is UiState.Success -> {
                    adapter?.submitList(state.data.groupList)
                }
                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }
                is UiState.Loading -> {}
                is UiState.Empty -> {}
                else -> {}
            }
        }
    }

    private fun storeGroup(department: String, groupId: Long) {
        viewModel.setGroupInfo(department, groupId)
        viewModel.clearDepartmentData()
        dismiss()
    }

    private fun setClickToDepartmentForm() {
        binding.tvDepartmentAdd.setOnClickListener {
            Intent(Intent.ACTION_VIEW, Uri.parse(DEPARTMENT_FORM_URL)).apply {
                startActivity(this)
            }
        }
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    private fun setListWithInfinityScroll() {
        binding.rvDepartmentList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvDepartmentList.canScrollVertically(1) &&
                            layoutManager is LinearLayoutManager &&
                            layoutManager.findLastVisibleItemPosition() == adapter!!.itemCount - 1
                        ) {
                            viewModel.getGroupList(inputText)
                        }
                    }
                }
            }
        })
    }

    private fun setHideKeyboard() {
        binding.layoutDepartmentDialog.setOnSingleClickListener {
            requireContext().hideKeyboard(
                requireView(),
            )
        }
    }

    private fun recyclerviewScroll() {
        binding.rvDepartmentList.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_MOVE -> {
                    binding.layoutDepartmentDialog.requestDisallowInterceptTouchEvent(true)
                }
            }
            return@setOnTouchListener false
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogDepartmentFragment()
        private const val DEPARTMENT_FORM_URL = "https://bit.ly/3pO0ijD"
    }
}
