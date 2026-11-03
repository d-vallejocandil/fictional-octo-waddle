package com.example.fictional_octo_waddle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fictional_octo_waddle.databinding.AchievementBinding
import com.example.fictional_octo_waddle.databinding.ActivityAchievementsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AchievementsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAchievementsBinding
    lateinit var adapter: AchievementAdapter
    val api = Api.get()
    lateinit var achievementDAO: AchievementDAO
    lateinit var gameDAO: GameDAO
    var gameid: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAchievementsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        gameid = intent.getIntExtra("GAME", -1)
        gameDAO = GameDAO(this)
        val game = gameDAO.get(gameid)!!
        supportActionBar?.title = game.name
        supportActionBar?.subtitle = gameid.toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.recycler.layoutManager = LinearLayoutManager(this)
        achievementDAO = AchievementDAO(this)
        val achievements = achievementDAO.get(gameid)
        adapter = AchievementAdapter(achievements)
        binding.recycler.adapter = adapter
        update(achievements)
        var achieved = 0
        for (achievement in achievements) {
            if (achievement.achieved == 1) {
                achieved = achieved + 1
            }
        }
        if (achievements.isEmpty()) {
            binding.percent.text = "Empty"
        } else {
            binding.percent.text = "$achieved / ${achievements.size}"
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sync) {
            updateachievements()
            return true
        } else if(item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    fun update(achievements: List<Achievement>) {
        adapter.update(achievements)
    }
    fun updateachievements() {
        achievementDAO.delete(gameid)
        val game = gameDAO.get(gameid)!!
        CoroutineScope(Dispatchers.IO).launch {
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
                    achievementDAO.insert(Achievement(-1, achievement1.name!!, achievement1.hidden!!, description, achievement1.icon!!, achievement1.icongray!!, achieved, game))
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                val achievements = achievementDAO.get(gameid)
                update(achievements)
                var achieved = 0
                for (achievement in achievements) {
                    if (achievement.achieved == 1) {
                        achieved = achieved + 1
                    }
                }
                if (achievements.isEmpty()) {
                    binding.percent.text = "Empty"
                } else {
                    binding.percent.text = "$achieved / ${achievements.size}"
                }
            }
        }
    }
}