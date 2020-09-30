package com.zywczas.bestonscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.model.Movie
import java.util.*

class MovieAdapter(
    private val context: Context,
    private val picasso: Picasso,
    private val itemClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }
}) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val posterImage: ImageView = itemView.findViewById(R.id.posterImageViewListItem)
        private val title: TextView = itemView.findViewById(R.id.titleTextViewListItem)
        private val rate: TextView = itemView.findViewById(R.id.rateTextViewListItem)

        fun bindMovie(movie: Movie) {
            title.text = movie.title
            rate.text = String.format(Locale.getDefault(), "%.1f", movie.voteAverage)
            val posterPath = "https://image.tmdb.org/t/p/w200" + movie.posterPath
            picasso.load(posterPath)
                .error(R.drawable.error_image)
                .into(posterImage)
            itemView.setOnClickListener { itemClick(movie) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //todo dac parent.context i wtedy chyba nie potrzeba dawac contextu w construktorze
        val view = LayoutInflater.from(context).inflate(R.layout.movies_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMovie(getItem(position))
    }

}