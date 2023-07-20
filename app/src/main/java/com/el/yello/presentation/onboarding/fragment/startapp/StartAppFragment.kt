package com.el.yello.presentation.onboarding.fragment.startapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.el.yello.R
import com.el.yello.databinding.FragmentStartappBinding
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener
import com.el.yello.presentation.main.MainActivity

class StartAppFragment : BindingFragment<FragmentStartappBinding>(R.layout.fragment_startapp) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartYello.setOnSingleClickListener {
            Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
            requireActivity().finish()
        }
    }
}
