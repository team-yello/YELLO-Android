package com.el.yello.presentation.main.profile.quitReason

import android.os.Bundle
import androidx.activity.viewModels
import com.el.yello.R
import com.el.yello.databinding.ActivityProfileQuitReasonBinding
import com.el.yello.presentation.main.profile.ProfileViewModel
import com.example.ui.base.BindingActivity

class QuitReasonActivity :
    BindingActivity<ActivityProfileQuitReasonBinding>(R.layout.activity_profile_quit_reason) {

    private val viewModel by viewModels<ProfileViewModel>()

    private lateinit var addQuitReasonList: List<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        initQuitReasonAdapter()
    }

    private fun initQuitReasonAdapter() {
        viewModel.addQuitReasonList()
        addQuitReasonList = viewModel.quitReasonList.value ?: emptyList()
        val adapter = QuitReasonAdapter(storeQuitReason = ::storeQuitReason)
        binding.rvQuitReason.adapter = adapter
        adapter.submitList(addQuitReasonList)
    }

    private fun storeQuitReason(reason: String) {
        viewModel.setQuitReason(reason)
    }
}
