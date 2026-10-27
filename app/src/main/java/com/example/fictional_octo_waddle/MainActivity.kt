package com.example.fictional_octo_waddle

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    var games: List<OwnedGame> = emptyList()
    val api = Api.get()
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
        adapter = GameAdapter(games) {
            val game = games[it]
            val intent = Intent(this, AchievementsActivity::class.java)
            intent.putExtra("GAME", game.appid)
            startActivity(intent)
        }
        binding.recycler.adapter = adapter
        CoroutineScope(Dispatchers.IO).launch {
            val games = api.getGames(Keys.KEY, Keys.STEAM_ID).response.games
            CoroutineScope(Dispatchers.Main).launch {
                this@MainActivity.games = games
                update(games)
            }
        }
    }
    fun update(games: List<OwnedGame>) {
        adapter.update(games)
    }
}