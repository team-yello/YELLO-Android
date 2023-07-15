package com.yello.presentation.main.yello

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.ui.base.BindingFragment
import com.yello.R
import com.yello.databinding.FragmentYelloBinding
import com.yello.presentation.main.yello.start.YelloStartFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YelloFragment : BindingFragment<FragmentYelloBinding>(R.layout.fragment_yello) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragmentContainerView()
    }

    private fun initFragmentContainerView() {
        // TODO: 서버통신 후 screen 분기 처리
        navigateTo<YelloStartFragment>()
    }

    private inline fun <reified T : Fragment> navigateTo() {
        requireActivity().supportFragmentManager.commit {
            replace<T>(R.id.fcv_yello, T::class.java.canonicalName)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = YelloFragment()
    }
}
