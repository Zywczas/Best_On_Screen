package com.zywczas.bestonscreen.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.EXTRA_MOVIE
import com.zywczas.bestonscreen.utilities.showToast
import com.zywczas.bestonscreen.viewmodels.MovieDetailsVM
import com.zywczas.bestonscreen.viewmodels.factories.MovieDetailsVMFactory
import kotlinx.android.synthetic.main.activity_movie_details.*
import javax.inject.Inject

class MovieDetailsActivity : AppCompatActivity() {

    lateinit var movieDetailsVM: MovieDetailsVM
    @Inject
    lateinit var factory: MovieDetailsVMFactory
    @Inject
    lateinit var picasso: Picasso
    lateinit var movieFromParcel: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        App.moviesComponent.inject(this)
        movieDetailsVM = ViewModelProvider(this, factory).get(MovieDetailsVM::class.java)

        movieFromParcel = intent.getParcelableExtra(EXTRA_MOVIE)!!

        setupMovieUIDetails()
        checkIfMovieIsInDb()
    }

    @SuppressLint("SetTextI18n")
    private fun setupMovieUIDetails() {
        //downloading image of width 300 because tmdb Api doesn't support 250, resized in picasso to 250
        val posterPath = "https://image.tmdb.org/t/p/w300" + movieFromParcel.posterPath

        picasso.load(posterPath)
            .resize(250, 0)
            .error(R.drawable.error_image)
            .into(posterImageViewDetails)

        titleTextViewDetails.text = movieFromParcel.title
        rateTextViewDetails.text = "Rate: ${movieFromParcel.voteAverage.toString()}"
        releaseDateTextViewDetails.text = "Release date: ${movieFromParcel.releaseDate}"
        overviewTextViewDetails.text = movieFromParcel.overview
        genresTextViewDetails.text = movieDetailsVM.getGenresDescription(movieFromParcel)
    }

    fun addToListClicked(view: View) {

        movieDetailsVM.addDeleteMovie(movieFromParcel, addToListBtn.tag.toString())
            .observe(this, Observer {
                it.getContentIfNotHandled()?.let { m -> showToast(m) }
            })

    }

    private fun checkIfMovieIsInDb() {
        movieDetailsVM.checkIfMovieIsInDb(movieFromParcel.id!!).observe(this,
            Observer {
                it.getContentIfNotHandled()?.let { boolean ->
                    addToListBtn.isChecked = boolean
                    //this tag is used in addToListClicked() so it knows whether to add or delete movie
                    addToListBtn.tag = boolean.toString()
                }
            })
    }

    override fun onDestroy() {
        movieDetailsVM.clearDisposables()
        super.onDestroy()
    }
}
