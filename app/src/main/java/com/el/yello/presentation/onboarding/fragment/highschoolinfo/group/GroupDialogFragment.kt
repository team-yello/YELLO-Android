package com.el.yello.presentation.onboarding.fragment.highschoolinfo.group

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentDialogGroupBinding
import com.el.yello.presentation.onboarding.OnBoardingViewModel
import com.example.ui.base.BindingBottomSheetDialog

class GroupDialogFragment :
    BindingBottomSheetDialog<FragmentDialogGroupBinding>(R.layout.fragment_dialog_group) {

    private lateinit var groupList: List<String>

    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initGroupAdapter()
    }

    private fun initGroupAdapter() {
        viewModel.addHighSchoolGroup()
        groupList = viewModel.highSchoolGroupList.value ?: emptyList()
        val adapter = GroupDialogAdapter(storeHighSchoolGroup = ::storeHighSchoolGroup)
        binding.rvGroup.adapter = adapter
        adapter.submitList(groupList)
    }

    private fun storeHighSchoolGroup(group: String) {
        viewModel.setGroupHighSchoolInfo(group)
        dismiss()
    }

    companion object {
        @JvmStatic
        fun newInstance() = GroupDialogFragment()
    }
}
