package com.el.yello.presentation.onboarding.fragment.startapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.el.yello.R
import com.el.yello.databinding.FragmentStartAppBinding
import com.el.yello.presentation.main.MainActivity
import com.example.ui.base.BindingFragment
import com.example.ui.view.setOnSingleClickListener

class StartAppFragment : BindingFragment<FragmentStartAppBinding>(R.layout.fragment_start_app) {
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
