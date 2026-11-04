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
import androidx.appcompat.widget.SearchView
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
    var games: List<Game> = emptyList()
    lateinit var session: Session
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
        supportActionBar?.setTitle(R.string.home)
        session = Session(this)
        supportActionBar?.subtitle = session.get()
        gameDAO = GameDAO(this)
        games = gameDAO.get()
        adapter = GameAdapter(games) {
            val game = games[it]
            val intent = Intent(this, AchievementsActivity::class.java)
            intent.putExtra("GAME", game.id)
            startActivity(intent)
        }
        binding.recycler.adapter = adapter
        if (games.isEmpty()) {
            binding.total.setText(R.string.total_empty)
        } else {
            binding.total.text = "TOTAL: ${games.size}"
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    games = games.filter {
                        it.name.contains(newText, true)
                    }
                    update(games)
                }
                return true
            }
        })
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sync) {
            updategames()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    fun update(games: List<Game>) {
        adapter.update(games)
    }
    fun updategames() {
        gameDAO.delete()
        CoroutineScope(Dispatchers.IO).launch {
            val ownedgames = api.getGames(Keys.KEY, session.get()).response.games
            for (game in ownedgames) {
                gameDAO.insert(Game(-1, game.appid, game.name, game.img_icon_url))
            }
            CoroutineScope(Dispatchers.Main).launch {
                games = gameDAO.get()
                adapter.update(games)
                if (games.isEmpty()) {
                    binding.total.setText(R.string.total_empty)
                } else {
                    binding.total.text = "TOTAL: ${games.size}"
                }
            }
        }
    }
}