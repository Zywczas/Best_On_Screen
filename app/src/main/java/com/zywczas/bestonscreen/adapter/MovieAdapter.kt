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
import com.zywczas.bestonscreen.model.webservice.MovieFromApi
import java.util.*
import kotlin.collections.ArrayList

class MovieAdapter(
    private val context: Context,
    private val picasso: Picasso,
    private val itemClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.ViewHolder>(DIFF_CALLBACK) {

    //DIFF_CALLBACK as static val so it can be created before ListAdapter and passed to the constructor
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            //checks if object is the same, 'id' is unique so if ids are the same then the same object
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            //checks if value is the same, could be used on 'id' but for practice 'title: String' is used
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val posterImage: ImageView = itemView.findViewById(R.id.posterImageViewMovies)
        private val title: TextView = itemView.findViewById(R.id.titleTextViewMovies)
        private val rate: TextView = itemView.findViewById(R.id.rateTextViewMovies)

        fun bindMovie(movie: Movie) {
            title.text = movie.title
            rate.text = String.format(Locale.getDefault(), "%.1f", movie.voteAverage)
            //downloading image of width 200
            val posterPath = "https://image.tmdb.org/t/p/w200" + movie.posterPath

            picasso.load(posterPath)
                .error(R.drawable.error_image)
                .into(posterImage)

            itemView.setOnClickListener { itemClick(movie) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < itemCount && position != RecyclerView.NO_POSITION) {
            holder.bindMovie(getItem(position))
        }
    }

}