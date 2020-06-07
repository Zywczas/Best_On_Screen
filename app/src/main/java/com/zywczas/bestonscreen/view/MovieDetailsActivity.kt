package com.zywczas.bestonscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.App
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
    lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        App.moviesComponent.inject(this)

        movieDetailsVM = ViewModelProvider(this, factory).get(MovieDetailsVM::class.java)

        setupMovieUIDetails()
        checkIfMovieIsInDb()
        setupMessageObserver()
    }

    fun addToListClicked (view: View) {

        if(addToListBtn.text.toString() == addToListBtn.textOn.toString()) {

            movieDetailsVM.addMovieToDb(movie).observe(this,
                Observer { it.getContentIfNotHandled()?.let {message ->
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            })

        } else if (addToListBtn.text.toString() == addToListBtn.textOff.toString()) {

            movieDetailsVM.deleteMovieFromDb(movie).observe(this,
            Observer { it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
            })
        }
    }

    private fun setupMessageObserver(){

    }

    private fun setupMovieUIDetails() {
        movie = intent.getParcelableExtra(EXTRA_MOVIE)!!
        //downloading image of width 300 because tmdb Api doesn't support 250, resized in picasso to 250
        val posterPath = "https://image.tmdb.org/t/p/w300" + movie.posterPath

        picasso.load(posterPath)
            .resize(250, 0)
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
    }

    private fun checkIfMovieIsInDb(){
        if (movie.id != null) {
            movieDetailsVM.checkIfMovieIsInDb(movie.id!!).observe(this,
            Observer { it.getContentIfNotHandled()?.let {rowsQuantity ->
                Log.d("film test", "movie details: $rowsQuantity")
                if (rowsQuantity == 1) {
                    addToListBtn.isChecked = true
                } else if (rowsQuantity == 0) {
                    addToListBtn.isChecked = false
                }
            }
            })

        }
    }

    override fun onDestroy() {
        movieDetailsVM.clear()
        super.onDestroy()
    }
}
