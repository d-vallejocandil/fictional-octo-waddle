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
        gameDAO = GameDAO(this)
        achievementDAO = AchievementDAO(this)
        CoroutineScope(Dispatchers.IO).launch {
            val games = api.getGames(Keys.KEY, Keys.STEAM_ID).response.games
            for (game in games) {
                var check1: AvailableGameStats? = null
                check1 = api.getAchievements(
                    Keys.KEY,
                    game.appid
                ).game.availableGameStats
                var achievements: List<GameAchievement> = emptyList()
                if(check1 != null) {
                    achievements = check1.achievements
                }
                var check2: PlayerStats? = null
                check2 = api.getUserAchievements(
                    Keys.KEY,
                    Keys.STEAM_ID,
                    game.appid
                ).stats
                var userachievements: List<UserAchievement> = emptyList()
                if(check2 != null) {
                    userachievements = check2.achievements
                }
                CoroutineScope(Dispatchers.Main).launch {
                    val newgame = Game(game.appid, game.name, game.img_icon_url)
                    gameDAO.insert(newgame)
                    if (achievements.size > 0) {
                        for (achievement in achievements) {
                            var achieved = 0
                            /*if (userachievements.size > 0) {
                                for (userachievement in userachievements) {
                                    if (userachievement.name == achievement.name) {
                                        achieved = 1
                                    }
                                }
                            }*/
                            achievementDAO.insert(
                                Achievement(
                                    achievement.name,
                                    achievement.hidden,
                                    achievement.description,
                                    achievement.icon,
                                    achievement.icongray,
                                    achieved,
                                    newgame
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}