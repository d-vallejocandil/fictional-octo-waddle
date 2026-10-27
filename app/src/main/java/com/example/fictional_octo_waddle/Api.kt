package com.example.fictional_octo_waddle

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("IPlayerService/GetOwnedGames/v1/")
    suspend fun getGames(
        @Query("key") key: String,
        @Query("steamid") steamid: String,
        @Query("include_appinfo") include_appinfo: Boolean = true,
        @Query("include_played_free_games") include_played_free_games: Boolean = false
    ): GetOwnedGames
    @GET("ISteamUserStats/GetSchemaForGame/v2/")
    suspend fun getAchievements(
        @Query("key") key: String,
        @Query("appid") appid: Int
    ): GetSchemaForGame
    @GET("ISteamUserStats/GetUserStatsForGame/v2/")
    suspend fun getUserAchievements(
        @Query("key") key: String,
        @Query("steamid") steamid: String,
        @Query("appid") appid: Int
    ): GetUserStatsForGame
    companion object {
        fun get(): Api {
            return Retrofit.Builder().baseUrl("https://api.steampowered.com/").addConverterFactory(GsonConverterFactory.create()).build().create(Api::class.java)
        }
    }
}