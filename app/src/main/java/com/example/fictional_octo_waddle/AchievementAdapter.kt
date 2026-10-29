package com.example.fictional_octo_waddle

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fictional_octo_waddle.databinding.AchievementBinding
import com.example.fictional_octo_waddle.databinding.GameBinding
import com.squareup.picasso.Picasso

class AchievementAdapter(var achievements: List<Achievement>): RecyclerView.Adapter<AchievementViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = AchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AchievementViewHolder(binding)
    }
    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val achievement = achievements[position]
        holder.render(achievement)
    }
    override fun getItemCount(): Int {
        return achievements.size
    }
    fun update(achievements: List<Achievement>) {
        this.achievements = achievements
        notifyDataSetChanged()
    }
}
class AchievementViewHolder(val binding: AchievementBinding): RecyclerView.ViewHolder(binding.root) {
    fun render(achievement: Achievement) {
        if (achievement.hidden == 1) {
            Picasso.get().load(achievement.icongray).into(binding.image)
            binding.description.text = "Hidden description."
        } else {
            Picasso.get().load(achievement.icon).into(binding.image)
            binding.description.setOnClickListener {
                binding.description.text = achievement.description
            }
        }
        if (achievement.achieved == 1) {
            binding.description.setTextColor(Color.GREEN)
        } else {
            binding.description.setTextColor(Color.RED)
        }
    }
}