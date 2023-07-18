package com.yello.presentation.onboarding.fragment.school.university

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.context.hideKeyboard
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentDialogSchoolBinding
import com.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.yello.util.context.yelloSnackbar

class SearchDialogFragment :
    BindingBottomSheetDialog<FragmentDialogSchoolBinding>(R.layout.fragment_dialog_school) {
    private lateinit var schoolList: List<String>
    private var adapter: SchoolAdapter? = null

    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        initView()
        setupSchoolData()
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private fun initView() {
        setHideKeyboard()
        binding.etSchoolSearch.doAfterTextChanged { input ->
            viewModel.getSchoolList(input.toString())
        }
        schoolList = viewModel.schoolResult.value ?: emptyList()
        adapter = SchoolAdapter(requireContext(), storeSchool = ::storeSchool)
        binding.rvSchoolList.adapter = adapter
    }

    private fun setHideKeyboard() {
        binding.layoutSchoolDialog.setOnSingleClickListener { requireContext().hideKeyboard(requireView()) }
    }

    private fun setupSchoolData() {
        viewModel.schoolData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> { yelloSnackbar(binding.root, getString(R.string.msg_error)) }
                is UiState.Loading -> {}
                is UiState.Empty -> {} // TODO: 조회 결과 없음 UI 처리
                is UiState.Success -> {
                    adapter?.submitList(state.data.schoolList)
                }
            }
        }
    }

    private fun storeSchool(school: String) {
        viewModel.setSchool(school)
        dismiss()
        binding.layoutSchoolDialog.setOnSingleClickListener {
            dismiss()
        }
        binding.btnBackDialog.setOnSingleClickListener {
            dismiss()
        }
    }



    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogFragment()
    }
}
