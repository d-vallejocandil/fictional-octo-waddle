package com.example.fictional_octo_waddle

data class GetOwnedGames(val response: OwnedGames)
data class OwnedGames(val games: List<OwnedGame>)
data class OwnedGame(val appid: Int, val name: String, val img_icon_url: String)