package com.example.fictional_octo_waddle

import android.app.appsearch.GetSchemaResponse

data class GetSchemaForGame(val game: Game)
data class Game(val availableGameStats: AvailableGameStats)
data class AvailableGameStats(val achievements: List<Achievement>)
data class Achievement(val name: String, val hidden: Int, val description: String)