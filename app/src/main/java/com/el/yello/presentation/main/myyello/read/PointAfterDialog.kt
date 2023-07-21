package com.el.yello.presentation.main.myyello.read

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.DialogPointAfterBinding
import com.el.yello.util.Utils
import com.example.domain.enum.PointEnum
import com.example.ui.base.BindingDialogFragment
import com.example.ui.fragment.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PointAfterDialog :
    BindingDialogFragment<DialogPointAfterBinding>(R.layout.dialog_point_after) {
    private val viewModel by activityViewModels<MyYelloReadViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observe()
        setDialogBackground()
        initEvent()
    }

    private fun initView() {
        if (viewModel.pointType == PointEnum.KEYWORD.ordinal) {
            viewModel.checkKeyword()
        } else {
            viewModel.checkInitial()
        }
        setDataView()
        binding.tvPoint.text = viewModel.myPoint.toString()
    }

    private fun setDataView() {
        binding.tvSubTitle.isVisible = viewModel.pointType == PointEnum.INITIAL.ordinal
        if (viewModel.pointType == PointEnum.KEYWORD.ordinal) {
            binding.tvTitle.text = "쪽지의 키워드를 얻었어요!"
        }
    }

    private fun observe() {
        viewModel.keywordData.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        binding.tvPoint.text = viewModel.minusPoint().toString()
                        binding.tvInitial.text = it.data.answer
                        viewModel.setIsFinishCheck(true)
                        viewModel.setHintUsed(true)
                    }

                    is UiState.Failure -> {
                        toast(it.msg)
                    }

                    else -> {}
                }

            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.nameData.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        binding.tvPoint.text = viewModel.minusPoint().toString()
                        binding.tvInitial.text = Utils.setChosungText(it.data.name, 0)
                        viewModel.setIsFinishCheck(true)
                        viewModel.setNameIndex(it.data.index)
                    }

                    is UiState.Failure -> {
                        toast(it.msg)
                    }

                    else -> {}
                }

            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initEvent() {
        binding.tvOk.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun setDialogBackground() {
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
        val dialogHorizontalMargin = (Resources.getSystem().displayMetrics.density * 16) * 2

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
        fun newInstance() =
            PointAfterDialog()
    }
}
