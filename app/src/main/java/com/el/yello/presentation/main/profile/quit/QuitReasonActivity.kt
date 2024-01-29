package com.el.yello.presentation.main.profile.quit

import QuitReasonAdapter
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileQuitReasonBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.example.ui.base.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuitReasonActivity :
    BindingActivity<ActivityProfileQuitReasonBinding>(R.layout.activity_profile_quit_reason) {
    private lateinit var quitReasonList: List<String>
    private val viewModel by viewModels<ProfileViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        initQuitReasonAdapter()
    }

    private fun initQuitReasonAdapter() {
        viewModel.addQuitReasonList()
        quitReasonList = viewModel.quitReasonData.value ?: emptyList()
        Log.d("QuitReasonActivity", "quitReasonList size: ${quitReasonList.size}")

        val adapter = QuitReasonAdapter(storeQuitReason = ::storeQuitReason)

        // Adapter 초기화 시 로그 확인
        Log.d("QuitReasonActivity", "Adapter initialized")
        binding.rvQuitReason.adapter = adapter
        Log.d("QuitReasonActivity", "Adapter: $adapter")
        adapter.submitList(ArrayList(quitReasonList))
        Log.d("QuitReasonActivity", "Adapter item count: ${adapter.itemCount}")
    }

    private fun storeQuitReason(reason: String) {
        viewModel.setQuitReason(reason)
    }
}
