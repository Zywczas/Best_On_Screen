package com.zywczas.bestonscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.di.App
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.EXTRA_MOVIE
import com.zywczas.bestonscreen.viewModel.MovieDetailsVM
import com.zywczas.bestonscreen.viewModel.MovieDetailsVMFactory
import kotlinx.android.synthetic.main.activity_movie_details.*
import javax.inject.Inject

class MovieDetailsActivity : AppCompatActivity() {

    lateinit var movieDetailsVM: MovieDetailsVM
    @Inject lateinit var factory: MovieDetailsVMFactory
    @Inject lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        App.moviesComponent.inject(this)

        movieDetailsVM = ViewModelProvider(this, factory).get(MovieDetailsVM::class.java)

        setupDetails()
    }

    private fun setupDetails() {
        val movie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        if (movie != null) {
            val posterPath = "https://image.tmdb.org/t/p/w500" + movie.posterPath

            picasso.load(posterPath)
                .resize(500, 0)
                .error(R.drawable.error_image)
                .into(posterImageViewDetails)

            titleTextViewDetails.text = movie.title
            rateTextViewDetails.text = "Rate: ${movie.voteAverage.toString()}"
            releaseDateTextViewDetails.text = "Release date: ${movie.releaseDate}"
            genresTextViewDetails.text = "Genres: ${movie.genre1}"
            overviewTextViewDetails.text = movie.overview

            genresTextViewDetails.text = when (movie.genresAmount) {
                1 -> "Genre: ${movie.genre1}"
                2 -> "Genres: ${movie.genre1}, ${movie.genre2}"
                3 -> "Genres: ${movie.genre1}, ${movie.genre2}, ${movie.genre3}"
                4 -> "Genres: ${movie.genre1}, ${movie.genre2}, ${movie.genre3}, ${movie.genre4}"
                5 -> "Genres: ${movie.genre1}, ${movie.genre2}, ${movie.genre3}, ${movie.genre4}, ${movie.genre5}"
                else -> "no information about genres"
            }
        } else {
            Toast.makeText(this, "Problem with getting the movie", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        movieDetailsVM.clear()
        super.onDestroy()
    }
}
