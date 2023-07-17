package com.yello.presentation.main.myyello.read

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.domain.entity.YelloDetail
import com.example.domain.enum.PointEnum
import com.example.ui.base.BindingActivity
import com.example.ui.context.toast
import com.example.ui.intent.longExtra
import com.example.ui.view.UiState
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.ActivityMyYelloReadBinding
import com.yello.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MyYelloReadActivity :
    BindingActivity<ActivityMyYelloReadBinding>(R.layout.activity_my_yello_read) {
    private val id by longExtra()
    private val viewModel by viewModels<MyYelloReadViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initClick()
        observe()
    }

    private fun initView() {
        viewModel.getYelloDetail(id)
    }

    private fun initClick() {
        binding.tvInitialCheck.setOnSingleClickListener {
            PointUseDialog.newInstance(true, PointEnum.INITIAL.ordinal)
                .show(supportFragmentManager, "dialog")
        }

        binding.clSendCheck.setOnSingleClickListener {
            PointUseDialog.newInstance(true, PointEnum.KEYWORD.ordinal)
                .show(supportFragmentManager, "dialog")
        }

        binding.ivBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun observe() {
        viewModel.yelloDetailData.flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        binding.data = it.data
                        setData(it.data)
                    }

                    is UiState.Failure -> {
                        toast(it.msg)
                    }

                    else -> {

                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun setData(yello: YelloDetail) {
        binding.tvSendName.isVisible = yello.nameHint != -1
        binding.tvNameNotYet.isVisible = yello.nameHint == -1
        if (yello.nameHint >= 0) binding.tvSendName.text =
            Utils.setChosungText(yello.senderName, yello.nameHint)
        binding.tvGender.text =
            if (yello.senderGender.contains("MALE")) getString(R.string.my_yello_male) else getString(
                R.string.my_yello_female
            )
    }

    companion object {
        fun getIntent(context: Context, id: Long) =
            Intent(context, MyYelloReadActivity::class.java)
                .putExtra("id", id)
    }
}