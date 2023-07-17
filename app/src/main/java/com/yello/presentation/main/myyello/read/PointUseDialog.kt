package com.yello.presentation.main.myyello.read

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import com.example.domain.enum.PointEnum
import com.example.ui.base.BindingDialogFragment
import com.example.ui.intent.boolArgs
import com.example.ui.intent.intArgs
import com.yello.R
import com.yello.databinding.DialogPointUseBinding

class PointUseDialog : BindingDialogFragment<DialogPointUseBinding>(R.layout.dialog_point_use) {
    private val isTwoButton by boolArgs()
    private val pointType by intArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setDialogBackground()
    }

    private fun initView() {
        setButtonView()
    }

    private fun setButtonView() {
        binding.tvNo.isVisible = !isTwoButton
        if (!isTwoButton) {
            binding.tvNo.text = "투표하고 포인트 받기"
        } else {
            if (pointType == PointEnum.INITIAL.ordinal) {
                binding.tvTitle.text = "300 포인트로 초성을 얻을까요?"
            }
        }
    }

    private fun setDialogBackground() {
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
        val dialogHorizontalMargin = (Resources.getSystem().displayMetrics.density * 40) * 2

        dialog?.window?.apply {
            setLayout(
                (deviceWidth - dialogHorizontalMargin * 2).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
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
