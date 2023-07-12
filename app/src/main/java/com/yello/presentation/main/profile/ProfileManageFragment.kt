package com.yello.presentation.main.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileManageBinding

class ProfileManageFragment :
    BindingFragment<FragmentProfileManageBinding>(R.layout.fragment_profile_manage) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTransactionButton(binding.btnProfileManageBack, ProfileFragment())
        initTransactionButton(binding.btnProfileManageQuit, ProfileQuitFragment())

        binding.btnProfileManageCenter.setOnSingleClickListener {
            // TODO: 버튼 3개 링크 설정
        }

        binding.btnProfileManageLogout.setOnSingleClickListener {
            // TODO: 로그아웃 설정
        }
    }

    private fun initTransactionButton(view: View, fragment: Fragment) {
        view.setOnSingleClickListener {
            parentFragmentManager.beginTransaction().apply {
                setReorderingAllowed(true)
                replace(R.id.fcv_main, fragment)
            }.commit()
        }
    }
}