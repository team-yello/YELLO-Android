package com.yello.presentation.onboarding.fragment.studentid.dialog.department

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.context.hideKeyboard
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentDialogDepartmentBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.yello.util.context.yelloSnackbar
import timber.log.Timber

class SearchDialogDepartmentFragment :
    BindingBottomSheetDialog<FragmentDialogDepartmentBinding>(R.layout.fragment_dialog_department) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    private var adapter: DepartmentAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initDepartmentAdapter()
        setupDepartmentData()
        recyclerviewScroll()
        seClicktDepartmentform()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    private fun initDepartmentAdapter() {
        setHideKeyboard()
        binding.etDepartmentSearch.doAfterTextChanged { input ->
            viewModel.getGroupList(input.toString())
        }
        adapter = DepartmentAdapter(storeDepartment = ::storeGroup)
        binding.rvDepartmentList.adapter = adapter
        binding.btnBackDialog.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun setHideKeyboard() {
        binding.layoutDepartmentDialog.setOnSingleClickListener {
            requireContext().hideKeyboard(
                requireView(),
            )
        }
    }

    private fun setupDepartmentData() {
        viewModel.departmentData.observe(viewLifecycleOwner) { state ->
            Timber.d("GET GROUP LIST OBSERVE : $state")
            when (state) {
                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }

                is UiState.Loading -> {}
                is UiState.Empty -> {}
                is UiState.Success -> {
                    adapter?.submitList(state.data.groupList)
                }
            }
        }
    }

    private fun storeGroup(department: String, groupId: Long) {
        viewModel.setGroupInfo(department, groupId)
        viewModel.clearDepartmentData()
        dismiss()
    }

    @SuppressLint("ClickableViewAccessibility")
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

    private fun seClicktDepartmentform() {
        binding.tvDepartmentAdd.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https:/bit.ly/3pO0ijD"))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogDepartmentFragment()
    }
}
