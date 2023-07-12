package com.yello.presentation.main.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.yello.R
import com.yello.databinding.FragmentProfileQuitBinding

class ProfileQuitFragment :
    BindingFragment<FragmentProfileQuitBinding>(R.layout.fragment_profile_quit) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTransactionButton(binding.btnProfileQuitBack, ProfileManageFragment())
        initTransactionButton(binding.btnProfileQuitReturn, ProfileManageFragment())
        initTransactionButton(binding.btnProfileQuitResume, ProfileQuitForSureFragment())
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