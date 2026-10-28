package com.example.fictional_octo_waddle

data class GetUserStatsForGame(val stats: PlayerStats? = null)
data class PlayerStats(val achievements: List<UserAchievement>)
data class UserAchievement(val name: String, val achieved: Int)