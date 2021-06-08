package com.example.capstoneproject.onboarding.intro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.databinding.ItemOnboardingBinding

class OnboardingItemsAdapter(private val onboardingModels: List<OnboardingModel>) :
    RecyclerView.Adapter<OnboardingItemsAdapter.OnboardingItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnboardingItemViewHolder {
        val binding =
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return OnboardingItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OnboardingItemViewHolder,
        position: Int
    ) {
        holder.bind(onboardingModels[position])
    }

    override fun getItemCount(): Int = onboardingModels.size

    inner class OnboardingItemViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: OnboardingModel) {
            with(binding) {
                tvSubtitle.text = model.subtitle
            }
        }
    }
}