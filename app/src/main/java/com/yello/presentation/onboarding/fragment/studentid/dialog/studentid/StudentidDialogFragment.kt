package com.yello.presentation.onboarding.fragment.studentid.dialog.studentid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.domain.entity.MyFriend
import com.example.domain.entity.MyStudentid
import com.example.ui.base.BindingBottomSheetDialog
import com.yello.R
import com.yello.databinding.FragmentDialogStudentIdBinding
import com.yello.presentation.onboarding.fragment.addfriend.AddFriendAdapter
import com.yello.presentation.onboarding.fragment.addfriend.AddFriendViewModel

class StudentidDialogFragment :
    BindingBottomSheetDialog<FragmentDialogStudentIdBinding>(R.layout.fragment_dialog_student_id) {

    private lateinit var idList: List<MyStudentid>

    private val viewModel by viewModels<StudentidDialogVIewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initStudentidAdapter() {
        viewModel.addStudentId()
        idList = viewModel.studentidResult.value ?: emptyList()

    }

    companion object {
        @JvmStatic
        fun newInstance() = StudentidDialogFragment()
    }
}
