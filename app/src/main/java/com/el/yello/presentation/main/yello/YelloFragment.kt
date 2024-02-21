package com.el.yello.presentation.main.yello

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.el.yello.R
import com.el.yello.databinding.FragmentYelloBinding
import com.el.yello.presentation.main.yello.YelloState.Lock
import com.el.yello.presentation.main.yello.YelloState.Valid
import com.el.yello.presentation.main.yello.YelloState.Wait
import com.el.yello.presentation.main.yello.lock.YelloLockFragment
import com.el.yello.presentation.main.yello.start.YelloStartFragment
import com.el.yello.presentation.main.yello.vote.VoteActivity
import com.el.yello.presentation.main.yello.wait.YelloWaitFragment
import com.el.yello.util.extension.yelloSnackbar
import com.example.ui.base.BindingFragment
import com.example.ui.extension.restartApp
import com.example.ui.state.UiState.Empty
import com.example.ui.state.UiState.Failure
import com.example.ui.state.UiState.Loading
import com.example.ui.state.UiState.Success
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class YelloFragment : BindingFragment<FragmentYelloBinding>(R.layout.fragment_yello) {
    private val viewModel by activityViewModels<YelloViewModel>()

    private val voteResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_CANCELED) {
                yelloSnackbar(
                    binding.root,
                    getString(R.string.internet_connection_error_msg)
                )
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupYelloState()
        checkStoredVote()
    }

    private fun setupYelloState() {
        viewModel.yelloState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is Loading -> {}
                    is Success -> {
                        when (state.data) {
                            is Lock -> navigateTo<YelloLockFragment>()
                            is Valid -> navigateTo<YelloStartFragment>()
                            is Wait -> navigateTo<YelloWaitFragment>()
                        }
                    }

                    is Empty -> {
                        yelloSnackbar(
                            binding.root,
                            getString(R.string.internet_connection_error_msg),
                        )
                    }

                    is Failure -> {
                        restartApp(requireContext(), getString(R.string.token_expire_error_msg))
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private inline fun <reified T : Fragment> navigateTo() {
        requireActivity().supportFragmentManager.commit {
            replace<T>(R.id.fcv_yello, T::class.java.canonicalName)
        }
    }

    private fun checkStoredVote() {
        viewModel.getStoredVote() ?: return
        intentToVoteScreen()
    }

    private fun intentToVoteScreen() {
        voteResultLauncher.launch(Intent(activity, VoteActivity::class.java))
    }

    override fun onResume() {
        super.onResume()

        viewModel.getVoteState()
    }

    companion object {
        @JvmStatic
        fun newInstance() = YelloFragment()
    }
}
