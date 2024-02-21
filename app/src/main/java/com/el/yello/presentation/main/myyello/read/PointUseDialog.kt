package com.el.yello.presentation.main.myyello.read

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.el.yello.R
import com.el.yello.databinding.DialogPointUseBinding
import com.el.yello.presentation.main.MainActivity
import com.el.yello.util.amplitude.AmplitudeUtils
import com.example.domain.enum.PointEnum
import com.example.ui.base.BindingDialogFragment
import com.example.ui.extension.setOnSingleClickListener

class PointUseDialog : BindingDialogFragment<DialogPointUseBinding>(R.layout.dialog_point_use) {
    private val viewModel by activityViewModels<MyYelloReadViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setDialogBackground()
        initEvent()
    }

    private fun initView() {
        viewModel.setPointAndIsTwoButton(
            arguments?.getInt("point_type") ?: 0,
            arguments?.getBoolean("is_two_button") ?: false,
        )
        setDataView()
        binding.tvPoint.text = viewModel.myPoint.toString()
    }

    private fun setDataView() {
        binding.tvNo.isVisible = viewModel.isTwoButton
        binding.ivClose.isVisible = !viewModel.isTwoButton
        if (!viewModel.isTwoButton) {
            binding.tvOk.text = getString(R.string.dialog_vote_get_point)
        } else {
            when (viewModel.pointType) {
                PointEnum.INITIAL.ordinal -> {
                    binding.tvTitle.text = getString(R.string.dialog_get_initial_question)
                    binding.tvOk.text = getString(R.string.dialog_get_initial)
                }
                PointEnum.KEYWORD.ordinal -> {
                    binding.tvTitle.text = getString(R.string.dialog_get_keyword_question)
                    binding.tvOk.text = getString(R.string.dialog_get_keyword)
                }
                else -> {
                    binding.tvTitle.text = getString(R.string.dialog_get_initial_free_question)
                    binding.tvOk.text = getString(R.string.dialog_get_initial)
                }
            }
        }
    }

    private fun initEvent() {
        binding.tvOk.setOnSingleClickListener {
            if (binding.tvOk.text.contains("투표")) {
                Intent(activity, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(this)
                }
                requireActivity().finish()
            } else {
                dismiss()
                when (viewModel.pointType) {
                    PointEnum.INITIAL.ordinal -> {
                        AmplitudeUtils.trackEventWithProperties("click_modal_firstletter_yes")
                    }
                    PointEnum.SUBSCRIBE.ordinal -> {
                        AmplitudeUtils.trackEventWithProperties("click_modal_firstletter_yes")
                    }
                    PointEnum.KEYWORD.ordinal -> {
                        AmplitudeUtils.trackEventWithProperties("click_modal_keyword_yes")
                    }
                }
                PointAfterDialog.newInstance().show(parentFragmentManager, "dialog")
            }
        }

        binding.tvNo.setOnSingleClickListener {
            if (viewModel.pointType == PointEnum.INITIAL.ordinal) {
                AmplitudeUtils.trackEventWithProperties("click_modal_firstletter_no")
            } else {
                AmplitudeUtils.trackEventWithProperties("click_modal_keyword_no")
            }
            dismiss()
        }

        binding.ivClose.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun setDialogBackground() {
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
        val dialogHorizontalMargin = (Resources.getSystem().displayMetrics.density * 16) * 2

        dialog?.window?.apply {
            setLayout(
                (deviceWidth - dialogHorizontalMargin * 2).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.drawable.shape_fill_gray900_12dp)
        }
        dialog?.setCancelable(true)
    }

    companion object {
        @JvmStatic
        fun newInstance(isTwoButton: Boolean, pointType: Int) =
            PointUseDialog().apply {
                arguments = Bundle().apply {
                    putInt("point_type", pointType)
                    putBoolean("is_two_button", isTwoButton)
                }
            }
    }
}
