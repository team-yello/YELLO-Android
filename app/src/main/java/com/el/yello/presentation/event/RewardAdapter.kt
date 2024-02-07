package com.el.yello.presentation.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.el.yello.databinding.ItemEventRewardBinding
import com.example.domain.entity.event.Event.Reward
import com.example.ui.diff.DiffCallback

class RewardAdapter : ListAdapter<Reward, RewardAdapter.RewardViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder =
        RewardViewHolder(
            ItemEventRewardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        holder.setReward(getItem(position))
    }

    class RewardViewHolder(private val binding: ItemEventRewardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setReward(reward: Reward) {
            binding.ivEventReward.load(reward.imageUrl)
            binding.tvEventRewardDescription.text = reward.name
        }
    }

    companion object {
        private val diffUtil = DiffCallback<Reward>(
            onItemsTheSame = { old, new -> old.name == new.name },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
