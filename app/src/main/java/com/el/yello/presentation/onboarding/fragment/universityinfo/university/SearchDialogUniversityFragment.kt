package com.el.yello.presentation.onboarding.fragment.universityinfo.university

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.FragmentDialogUniversityBinding
import com.el.yello.presentation.onboarding.OnBoardingViewModel
import com.el.yello.util.extension.yelloSnackbar
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.extension.hideKeyboard
import com.example.ui.state.UiState
import com.example.ui.extension.setOnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchDialogUniversityFragment :
    BindingBottomSheetDialog<FragmentDialogUniversityBinding>(R.layout.fragment_dialog_university) {
    private var adapter: UniversityAdapter? = null
    private val viewModel by activityViewModels<OnBoardingViewModel>()
    private var inputText: String = ""
    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initUniversityDialogView()
        setupUniversityData()
        setClickToSchoolForm()
        setListWithInfinityScroll()
        recyclerviewScroll()
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

    private fun initUniversityDialogView() {
        setHideKeyboard()
        binding.etSchoolSearch.doAfterTextChanged { input ->
            searchJob?.cancel()
            searchJob = viewModel.viewModelScope.launch {
                delay(Companion.debounceTime)
                input?.toString()?.let { viewModel.getUniversityList(it) }
            }
        }
        adapter = UniversityAdapter(storeUniversity = ::storeUniversity)
        binding.rvSchoolList.adapter = adapter
        binding.btnBackDialog.setOnSingleClickListener {
            dismiss()
        }
    }
    private fun setupUniversityData() {
        viewModel.universityState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Success -> {
                        adapter?.submitList(state.data.schoolList)
                    }
                    is UiState.Failure -> {
                        yelloSnackbar(binding.root, getString(R.string.internet_connection_error_msg))
                    }
                    is UiState.Loading -> return@onEach
                    is UiState.Empty -> return@onEach
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun storeUniversity(school: String) {
        viewModel.setUniversity(school)
        viewModel.clearUniversityData()
        dismiss()
    }

    private fun setClickToSchoolForm() {
        binding.tvSchoolAdd.setOnClickListener {
            Intent(Intent.ACTION_VIEW, Uri.parse(SCHOOL_FORM_URL)).apply {
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
        binding.rvSchoolList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    recyclerView.layoutManager?.let { layoutManager ->
                        if (!binding.rvSchoolList.canScrollVertically(1) &&
                            (layoutManager is LinearLayoutManager) &&
                            (layoutManager.findLastVisibleItemPosition() == (adapter!!.itemCount - 1))
                        ) {
                            viewModel.getUniversityList(inputText)
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
    private fun recyclerviewScroll() {
        binding.rvSchoolList.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> {
                        binding.layoutSchoolDialog.requestDisallowInterceptTouchEvent(true)
                    }
                }
                return false
            }
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogUniversityFragment()
        const val SCHOOL_FORM_URL = "https://bit.ly/46Yv0Hc"
        private const val debounceTime = 500L
    }
}
