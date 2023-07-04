package com.yello.presentation.main.profile

import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentProfileBinding

class ProfileFragment : BindingFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
