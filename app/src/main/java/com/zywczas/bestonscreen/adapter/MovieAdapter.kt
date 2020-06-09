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
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.webservice.MovieFromApi
import java.util.*
import kotlin.collections.ArrayList

class MovieAdapter (private val context: Context,
//                    private val movies: ArrayList<Movie>,
                    private val picasso: Picasso, private val itemClick: (Movie) -> Unit)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var movies = ArrayList<Movie>()

    fun setMovies(movies: List<Movie>){
        this.movies = movies as ArrayList<Movie>
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val posterImage: ImageView = itemView.findViewById(R.id.posterImageViewMovies)
        private val title: TextView = itemView.findViewById(R.id.titleTextViewMovies)
        private val rate: TextView = itemView.findViewById(R.id.rateTextViewMovies)

        fun bindMovie (movie: Movie) {
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

    override fun getItemCount(): Int {
        return movies.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMovie(movies[position])
    }

}