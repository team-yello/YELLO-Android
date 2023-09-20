package com.el.yello.presentation.onboarding.fragment.highschoolinfo.group

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentDialogGroupBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
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
        viewModel.addGroup()
        groupList = viewModel.groupList.value ?: emptyList()
        val adapter = GroupDialogAdapter { groupId ->
            storeGroup(viewModel.groupText.value.toString(), groupId.toLong())
        }
        binding.rvGroup.adapter = adapter
        adapter.submitList(groupList)
        binding.rvGroup.setOnClickListener {
            viewModel.getHighSchoolList(viewModel.groupList.value.toString())
        }
        Log.d("minju2", viewModel.groupText.value.toString())
    }

    private fun storeGroup(group: String, groupId: Long) {
        viewModel.setGroupHighSchoolInfo(group, groupId)
        dismiss()
        Log.d("minju3", viewModel.groupText.value.toString())
    }

    companion object {
        @JvmStatic
        fun newInstance() = GroupDialogFragment()
    }
}
