package com.yello.presentation.main.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileBinding

class ProfileFragment : BindingFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnProfileAddGroup.setOnSingleClickListener {
            // TODO: 그룹 추가 로직
        }

        initTransactionButton(binding.btnProfileManage, ProfileManageFragment())
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
