package com.el.yello.presentation.main.myyello.read

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.DialogReadingTicketAfterBinding
import com.example.ui.base.BindingDialogFragment
import com.example.ui.fragment.toast
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
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
    }

    private fun observe() {
        viewModel.fullNameData.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        binding.tvKey.text = viewModel.myReadingTicketCount.toString()
                        binding.tvName.text = it.data.name
                        viewModel.setIsFinishCheck(true)
                        viewModel.setNameIndex(-2)
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
