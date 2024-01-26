package com.el.yello.presentation.main.profile.quitReason

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.databinding.ItemProfileQuitReasonBinding
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener

class QuitReasonAdapter(
    private val storeQuitReason: (String) -> Unit,
) : ListAdapter<String, QuitReasonAdapter.QuitReasonViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuitReasonViewHolder {
        return QuitReasonViewHolder(
            ItemProfileQuitReasonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            storeQuitReason,
        )
    }

    override fun onBindViewHolder(holder: QuitReasonViewHolder, position: Int) {
        holder.setQuitReason(getItem(position))
    }

    class QuitReasonViewHolder(
        private val binding: ItemProfileQuitReasonBinding,
        private val storeQuitReason: (String) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun setQuitReason(reason: String) {
            binding.tvQuitReasonList.text = reason
            binding.root.setOnSingleClickListener {
                storeQuitReason(reason)
                // TODO : 누르면 색깔 변경
            }
        }
    }

    companion object {
        private val diffUtil = ItemDiffCallback<String>(
            onItemsTheSame = { old, new -> old == new },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
