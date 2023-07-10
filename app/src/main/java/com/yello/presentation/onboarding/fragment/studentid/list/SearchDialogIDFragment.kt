package com.yello.presentation.onboarding.fragment.studentid.list

import android.os.Bundle
import android.view.View
import com.example.ui.base.BindingBottomSheetDialog
import com.yello.R
import com.yello.databinding.FragmentDialogStudentIdBinding

class SearchDialogidFragment :
    BindingBottomSheetDialog<FragmentDialogStudentIdBinding>(R.layout.fragment_dialog_student_id) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchDialogidFragment()
    }
}
