package com.example.fictional_octo_waddle

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fictional_octo_waddle.databinding.AchievementBinding
import com.example.fictional_octo_waddle.databinding.ActivityAchievementsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AchievementsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAchievementsBinding
    lateinit var adapter: AchievementAdapter
    var achievements: List<Achievement> = emptyList()
    val api = Api.get()
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
        val game = intent.getIntExtra("GAME", -1)
        adapter = AchievementAdapter(achievements)
        binding.recycler.adapter = adapter
        CoroutineScope(Dispatchers.IO).launch {
            val achievements = api.getAchievements(Keys.KEY, game).game.availableGameStats.achievements
            CoroutineScope(Dispatchers.Main).launch {
                this@AchievementsActivity.achievements = achievements
                update(achievements)
            }
        }
    }
    fun update(achievements: List<Achievement>) {
        adapter.update(achievements)
    }
}