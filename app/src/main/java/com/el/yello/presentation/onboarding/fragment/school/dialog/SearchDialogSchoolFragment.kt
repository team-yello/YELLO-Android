package com.el.yello.presentation.onboarding.fragment.school.dialog

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentDialogSchoolBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.util.context.yelloSnackbar
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.context.hideKeyboard
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class SearchDialogSchoolFragment :
    BindingBottomSheetDialog<FragmentDialogSchoolBinding>(R.layout.fragment_dialog_school) {
    private var adapter: SchoolAdapter? = null
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initView()
        setupSchoolData()
        setListWithInfinityScroll()
        recyclerviewScroll()
        setClickToSchoolForm()
    }

    // 바텀 시트 fullScreen
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

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    private fun initView() {
        setHideKeyboard()
        binding.etSchoolSearch.doAfterTextChanged { input ->
            viewModel.setInputText(input.toString())
            viewModel.getSchoolList(input.toString())
        }
        adapter = SchoolAdapter(storeSchool = ::storeSchool)
        binding.rvSchoolList.adapter = adapter
        binding.btnBackDialog.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun setListWithInfinityScroll() {
        binding.rvSchoolList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvSchoolList.canScrollVertically(1) &&
                            layoutManager is LinearLayoutManager &&
                            layoutManager.findLastVisibleItemPosition() == adapter!!.itemCount - 1
                        ) {
                            val inputText = viewModel.inputText.value
                            if (!inputText.isNullOrBlank()) {
                                viewModel.getSchoolList(inputText)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun setHideKeyboard() {
        binding.layoutSchoolDialog.setOnSingleClickListener {
            requireContext().hideKeyboard(
                requireView(),
            )
        }
    }

    private fun setupSchoolData() {
        viewModel.schoolData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    adapter?.submitList(state.data.schoolList)
                }
                is UiState.Failure -> {
                    yelloSnackbar(binding.root, getString(R.string.msg_error))
                }
                is UiState.Loading -> {}
                is UiState.Empty -> {}
            }
        }
    }

    private fun storeSchool(school: String) {
        viewModel.setSchool(school)
        viewModel.clearSchoolData()
        dismiss()
    }
    private fun recyclerviewScroll() {
        binding.rvSchoolList.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_MOVE -> {
                    binding.layoutSchoolDialog.requestDisallowInterceptTouchEvent(true)
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun setClickToSchoolForm() {
        binding.tvSchoolAdd.setOnClickListener {
            Intent(Intent.ACTION_VIEW, Uri.parse(SCHOOL_FORM_URL)).apply {
                startActivity(this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogSchoolFragment()
        private const val SCHOOL_FORM_URL = "https://bit.ly/46Yv0Hc"
    }
}
