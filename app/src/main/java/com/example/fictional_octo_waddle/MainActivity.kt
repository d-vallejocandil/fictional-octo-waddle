package com.example.fictional_octo_waddle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.CorrectionInfo
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fictional_octo_waddle.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: GameAdapter
    var games: List<Game> = emptyList()
    val api = Api.get()
    lateinit var gameDAO: GameDAO
    lateinit var achievementDAO: AchievementDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        gameDAO = GameDAO(this)
        val games = gameDAO.get()
        adapter = GameAdapter(games) {
            val game = games[it]
            val intent = Intent(this, AchievementsActivity::class.java)
            intent.putExtra("GAME", game.appid)
            startActivity(intent)
        }
        binding.recycler.adapter = adapter
        achievementDAO = AchievementDAO(this)
        CoroutineScope(Dispatchers.IO).launch {
            val games = api.getGames(Keys.KEY, Keys.STEAM_ID).response.games
            for (game in games) {
                val newGame = Game(game.appid, game.name, game.img_icon_url)
                gameDAO.insert(newGame)
                var achievements1 = emptyList<GameAchievement>()
                achievements1 = api.getAchievements(Keys.KEY, game.appid).game.availableGameStats?.achievements ?: emptyList()
                var achievements2 = emptyList<UserAchievement>()
                achievements2 = api.getUserAchievements(Keys.KEY, Keys.STEAM_ID, game.appid).playerstats?.achievements ?: emptyList()
                if (achievements1.size > 0) {
                    for (achievement1 in achievements1) {
                        var achieved = 0
                        if (achievements2.size > 0) {
                            for (achievement2 in achievements2) {
                                if (achievement1.name == achievement2.name) {
                                    achieved = 1
                                }
                            }
                        }
                        val description = if (achievement1.hidden == 1) "Hidden description." else achievement1.description!!
                        achievementDAO.insert(Achievement(achievement1.name!!, achievement1.hidden!!, description, achievement1.icon!!, achievement1.icongray!!, achieved, newGame))
                    }
                }
            }
        }
    }
}