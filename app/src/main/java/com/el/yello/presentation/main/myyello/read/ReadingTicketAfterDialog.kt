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
import com.el.yello.databinding.DialogReadingTicketAfterBinding
import com.example.ui.base.BindingDialogFragment
import com.example.ui.extension.toast
import com.example.ui.state.UiState
import com.example.ui.extension.setOnSingleClickListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ReadingTicketAfterDialog :
    BindingDialogFragment<DialogReadingTicketAfterBinding>(R.layout.dialog_reading_ticket_after) {
    private val viewModel by activityViewModels<MyYelloReadViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observe()
        setDialogBackground()
        initEvent()
    }

    private fun initView() {
        viewModel.postFullName()
        binding.tvName.visibility =View.INVISIBLE
    }

    private fun observe() {
        viewModel.fullNameData.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        binding.tvKey.text = viewModel.myReadingTicketCount.toString()
                        setAnswerWithFadeIn(it.data.name)
                        viewModel.getYelloDetail()
                        viewModel.setNameIndex(-2)
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
        binding.tvName.startAnimation(animation)
        binding.tvName.text = text
        binding.tvName.isVisible = true
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
        fun newInstance() =
            ReadingTicketAfterDialog()
    }
}
