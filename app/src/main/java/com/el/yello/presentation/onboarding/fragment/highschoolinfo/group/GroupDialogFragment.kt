package com.el.yello.presentation.onboarding.fragment.highschoolinfo.group

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.FragmentDialogGroupBinding
import com.el.yello.presentation.onboarding.activity.OnBoardingViewModel
import com.el.yello.util.context.yelloSnackbar
import com.example.domain.entity.onboarding.GroupHighSchool
import com.example.ui.base.BindingBottomSheetDialog
import com.example.ui.view.UiState
import timber.log.Timber

class GroupDialogFragment :
    BindingBottomSheetDialog<FragmentDialogGroupBinding>(R.layout.fragment_dialog_group) {

    private lateinit var groupList: List<GroupHighSchool>
    private var adapter: GroupDialogAdapter? = null

    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initGroupAdapter()
    }

    private fun initGroupAdapter() {
        viewModel.addGroup()
        groupList = viewModel.groupResult.value ?: emptyList()
        val adapter = GroupDialogAdapter(storeGroup = ::storeGroup)
        binding.rvGroup.adapter = adapter
        adapter.submitList(groupList)
        setupGroupData()
    }

    private fun setupGroupData() {
        viewModel.groupData.observe(viewLifecycleOwner) { state ->
            Timber.d("GET GROUP LIST OBSERVE : $state")
            when (state) {
                is UiState.Success -> {
                    adapter?.submitList(state.data.groupList)
                    viewModel.getHighGroupList(viewModel.groupData.value.toString())
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

    fun storeGroup(group: String, groupId: Long) {
        viewModel.setGroupHighSchoolInfo(group, groupId)

        dismiss()
    }

    companion object {
        @JvmStatic
        fun newInstance() = GroupDialogFragment()
    }
}
