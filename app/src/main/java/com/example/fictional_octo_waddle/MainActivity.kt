package com.example.fictional_octo_waddle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
    val api = Api.get()
    lateinit var gameDAO: GameDAO
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
            intent.putExtra("GAME", game.id)
            startActivity(intent)
        }
        binding.recycler.adapter = adapter
        if (games.isEmpty()) {
            binding.percent.text = "Empty"
        } else {
            binding.percent.text = games.size.toString()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sync) {
            updategames()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        val games = gameDAO.get()
        update(games)
        if (games.isEmpty()) {
            binding.percent.text = "Empty"
        } else {
            binding.percent.text = games.size.toString()
        }
    }
    fun update(games: List<Game>) {
        adapter.update(games)
    }
    fun updategames() {
        gameDAO.delete()
        CoroutineScope(Dispatchers.IO).launch {
            val games = api.getGames(Keys.KEY, Keys.STEAM_ID).response.games
            for (game in games) {
                gameDAO.insert(Game(-1, game.appid, game.name, game.img_icon_url))
            }
            CoroutineScope(Dispatchers.Main).launch {
                val games = gameDAO.get()
                adapter.update(games)
                if (games.isEmpty()) {
                    binding.percent.text = "Empty"
                } else {
                    binding.percent.text = games.size.toString()
                }
            }
        }
    }
}