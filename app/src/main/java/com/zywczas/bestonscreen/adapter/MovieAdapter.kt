package com.zywczas.bestonscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.model.webservice.MovieFromApi
import java.util.*
import kotlin.collections.ArrayList

class MovieAdapter (private val context: Context, private val movies: ArrayList<MovieFromApi>,
                    private val picasso: Picasso, private val itemClick: (MovieFromApi) -> Unit)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterImage = itemView.findViewById<ImageView>(R.id.posterImageViewMovies)
        val title = itemView.findViewById<TextView>(R.id.titleTextViewMovies)
        val rate = itemView.findViewById<TextView>(R.id.rateTextViewMovies)

        fun bindMovie (context: Context, movie: MovieFromApi) {
            title.text = movie.title
            rate.text = String.format(Locale.getDefault(), "%.1f", movie.voteAverage)
            val posterPath = "https://image.tmdb.org/t/p/w200" + movie.posterPath

            picasso.load(posterPath)
                .resize(200, 0)
                .error(R.drawable.error_image)
                .into(posterImage)

            itemView.setOnClickListener { itemClick(movie) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMovie(context, movies[position])
    }

}