package com.el.yello.presentation.main.profile.quit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.el.yello.R
import com.el.yello.databinding.ItemProfileQuitReasonBinding
import com.example.ui.view.ItemDiffCallback
import com.example.ui.view.setOnSingleClickListener

class QuitReasonAdapter(
    private val storeQuitReason: (String) -> Unit,
    private val onItemClickListener: (Int, Boolean) -> Unit
) : ListAdapter<String, QuitReasonAdapter.QuitReasonViewHolder>(diffUtil) {

    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuitReasonViewHolder {
        return QuitReasonViewHolder(
            ItemProfileQuitReasonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            storeQuitReason,
            this::onItemClicked,
        )
    }

    override fun onBindViewHolder(holder: QuitReasonViewHolder, position: Int) {
        holder.bind(getItem(position), position == selectedItemPosition)
    }

    private fun onItemClicked(position: Int) {
        if (selectedItemPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(selectedItemPosition)
        }
        selectedItemPosition = position
        notifyItemChanged(position)
        storeQuitReason(getItem(position))
        onItemClickListener(position, true)
    }

    class QuitReasonViewHolder(
        private val binding: ItemProfileQuitReasonBinding,
        private val storeQuitReason: (String) -> Unit,
        private val onItemClicked: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reason: String, isSelected: Boolean) {
            binding.reason = reason
            binding.root.setOnSingleClickListener {
                onItemClicked(absoluteAdapterPosition)
            }
            if (isSelected) {
                binding.ivQuitReasonCheckpoint.setBackgroundResource(R.drawable.ic_profile_quit_reason_check)
                binding.cvQuitReasonList.setBackgroundResource(R.drawable.shape_black_fill_white_line_8_rect)
                if (absoluteAdapterPosition == 7) {
                    binding.etQuitEtc.visibility = View.VISIBLE
                } else {
                    binding.etQuitEtc.visibility = View.GONE
                }
                storeQuitReason(reason)
            } else {
                binding.ivQuitReasonCheckpoint.setBackgroundResource(R.drawable.ic_profile_quit_reason_uncheck)
                binding.cvQuitReasonList.setBackgroundResource(R.drawable.shape_grayscales900_fill_8_rect)
                binding.etQuitEtc.visibility = View.GONE
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
