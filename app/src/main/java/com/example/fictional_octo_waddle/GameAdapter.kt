package com.example.fictional_octo_waddle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fictional_octo_waddle.databinding.GameBinding
import com.squareup.picasso.Picasso
import java.util.concurrent.RecursiveTask

class GameAdapter(var games: List<OwnedGame>, val listener: (Int) -> Unit): RecyclerView.Adapter<GameViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = GameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.render(game)
        holder.itemView.setOnClickListener {
            listener(position)
        }
    }
    override fun getItemCount(): Int {
        return games.size
    }
    fun update(games: List<OwnedGame>) {
        this.games = games
        notifyDataSetChanged()
    }
}
class GameViewHolder(val binding: GameBinding): RecyclerView.ViewHolder(binding.root) {
    fun render(game: OwnedGame) {
        Picasso.get().load("https://media.steampowered.com/steamcommunity/public/images/apps/${game.appid}/${game.img_icon_url}.jpg").into(binding.image)
        binding.name.text = game.name
    }
}