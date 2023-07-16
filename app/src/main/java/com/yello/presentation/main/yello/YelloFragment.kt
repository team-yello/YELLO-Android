package com.yello.presentation.main.yello

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.domain.entity.type.YelloState.Lock
import com.example.domain.entity.type.YelloState.Valid
import com.example.domain.entity.type.YelloState.Wait
import com.example.ui.base.BindingFragment
import com.example.ui.view.UiState.Empty
import com.example.ui.view.UiState.Failure
import com.example.ui.view.UiState.Loading
import com.example.ui.view.UiState.Success
import com.yello.R
import com.yello.databinding.FragmentYelloBinding
import com.yello.presentation.main.yello.lock.YelloLockFragment
import com.yello.presentation.main.yello.start.YelloStartFragment
import com.yello.presentation.main.yello.wait.YelloWaitFragment
import com.yello.util.context.yelloSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YelloFragment : BindingFragment<FragmentYelloBinding>(R.layout.fragment_yello) {
    val viewModel by viewModels<YelloViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupYelloState()
    }

    private fun setupYelloState() {
        viewModel.yelloState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Loading -> {}
                is Success -> {
                    when (state.data) {
                        is Lock -> navigateTo<YelloStartFragment>()
                        is Valid -> navigateTo<YelloStartFragment>()
                        is Wait -> navigateTo<YelloWaitFragment>()
                    }
                }

                is Empty -> {}
                is Failure -> yelloSnackbar(
                    binding.root,
                    getString(R.string.msg_failure),
                )
            }
        }
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
