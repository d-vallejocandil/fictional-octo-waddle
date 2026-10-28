package com.example.fictional_octo_waddle

import android.app.appsearch.GetSchemaResponse

data class GetSchemaForGame(val game: GameSchema)
data class GameSchema(val availableGameStats: AvailableGameStats? = null)
data class AvailableGameStats(val achievements: List<GameAchievement>)
data class GameAchievement(val name: String, val hidden: Int, val description: String, val icon: String, val icongray: String)