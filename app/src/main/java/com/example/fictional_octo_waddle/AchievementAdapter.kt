package com.example.fictional_octo_waddle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fictional_octo_waddle.databinding.AchievementBinding
import com.example.fictional_octo_waddle.databinding.GameBinding
import com.squareup.picasso.Picasso

class AchievementAdapter(var achievements: List<GameAchievement>): RecyclerView.Adapter<AchievementViewHolder>() {
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
    fun update(achievements: List<GameAchievement>) {
        this.achievements = achievements
        notifyDataSetChanged()
    }
}
class AchievementViewHolder(val binding: AchievementBinding): RecyclerView.ViewHolder(binding.root) {
    fun render(achievement: GameAchievement) {
        binding.name.text = achievement.name
        if (achievement.hidden == 1) {
            binding.description.text = "Hidden description."
        } else {
            binding.description.text = achievement.description
        }
    }
}