package com.el.yello.presentation.main.myyello.read

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
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
    }

    private fun setDataView() {
        binding.tvSubTitle.isVisible = viewModel.pointType == PointEnum.INITIAL.ordinal
        if (viewModel.pointType == PointEnum.KEYWORD.ordinal) {
            binding.tvTitle.text = getString(R.string.dialog_after_keyword_title)
        }
    }

    private fun observe() {
        viewModel.keywordData.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
                when (it) {
                    is UiState.Success -> {
                        binding.tvPoint.text = viewModel.myPoint.toString()
                        setAnswerWithFadeIn(it.data.answer)
                        viewModel.getYelloDetail()
                        viewModel.setHintUsed(true)
                    }

                    is UiState.Failure -> {
                        toast(it.msg)
                    }

                    else -> return@onEach
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.nameData.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
                when (it) {
                    is UiState.Success -> {
                        binding.tvPoint.text = viewModel.myPoint.toString()
                        binding.tvInitial.text = Utils.setChosungText(it.data.name, 0)
                        viewModel.getYelloDetail()
                        viewModel.setNameIndex(it.data.index)
                    }

                    is UiState.Failure -> {
                        toast(it.msg)
                    }

                    else -> return@onEach
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setAnswerWithFadeIn(text: String) {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        binding.tvInitial.startAnimation(animation)
        binding.tvInitial.text = text
        binding.tvInitial.isVisible = true
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
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.drawable.shape_fill_gray900_12dp)
        }
        dialog?.setCancelable(true)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PointAfterDialog()
    }
}
