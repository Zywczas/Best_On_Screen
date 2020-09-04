package com.zywczas.bestonscreen.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.CONFIGURATION_CHANGE
import com.zywczas.bestonscreen.utilities.EXTRA_MOVIE
import com.zywczas.bestonscreen.utilities.showToast
import com.zywczas.bestonscreen.viewmodels.DetailsVM
import com.zywczas.bestonscreen.viewmodels.factories.DetailsVMFactory
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailsVM
    @Inject
    lateinit var factory: DetailsVMFactory
    @Inject
    lateinit var picasso: Picasso
    private lateinit var movie: Movie
    private var wasOrientationChanged : Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        wasOrientationChanged = savedInstanceState?.getBoolean(CONFIGURATION_CHANGE)
        startDetailsActivitySetupChain()
    }

    private fun startDetailsActivitySetupChain(){
        injectDependencies{ isInjectionFinished ->
            if (isInjectionFinished) {
                getViewModelAndIntent { success ->
                    if (success) {
                        setupUIState()
                        setupAddToListBtnStateObserver()
                        setupIsMovieInDbObserverOnViewModelInit()
                    }
                }
            }
        }
    }

    private fun injectDependencies(complete: (Boolean) -> Unit){
        App.moviesComponent.inject(this)
        complete(true)
    }

    private fun getViewModelAndIntent(complete: (Boolean) -> Unit){
        val movieFromParcel = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        if (movieFromParcel != null) {
            movie = movieFromParcel
            viewModel = ViewModelProvider(this, factory).get(DetailsVM::class.java)
            complete(true)
        } else {
            addToListBtn.isVisible = false
            showToast("Cannot load the movie. Go back and try again.")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUIState() {
            val posterPath = "https://image.tmdb.org/t/p/w300" + movie.posterPath
            picasso.load(posterPath)
                .resize(250, 0)
                .error(R.drawable.error_image)
                .into(posterImageViewDetails)
            titleTextViewDetails.text = movie.title
            rateTextViewDetails.text = "Rate: ${movie.voteAverage}"
            releaseDateTextViewDetails.text = "Release date: ${movie.releaseDate}"
            overviewTextViewDetails.text = movie.overview
            genresTextViewDetails.text = getGenresDescription()
    }

    private fun getGenresDescription() : String {
        return when (movie.assignedGenresAmount) {
            1 -> "Genre: ${movie.genre1}"
            2 -> "Genres: ${movie.genre1}, ${movie.genre2}"
            3 -> "Genres: ${movie.genre1}, ${movie.genre2}, ${movie.genre3}"
            4 -> "Genres: ${movie.genre1}, ${movie.genre2}, ${movie.genre3}, ${movie.genre4}"
            5 -> "Genres: ${movie.genre1}, ${movie.genre2}, ${movie.genre3}, ${movie.genre4}, ${movie.genre5}"
            else -> "no information"
        }
    }

    private fun setupAddToListBtnStateObserver() {
        viewModel.isMovieInDbLD.observe(this,
            Observer {isInDb ->
                addToListBtn.isChecked = isInDb
                addToListBtn.tag = isInDb
            })
    }

    private fun setupIsMovieInDbObserverOnViewModelInit(){
        if (wasOrientationChanged == null) {
            viewModel.checkIfIsInDb(movie.id)
        }
    }

    fun addToListClicked(view: View) {
        val isButtonChecked = addToListBtn.tag as Boolean
        viewModel.addOrDeleteMovie(movie, isButtonChecked)
            .observe(this, Observer {
                it.getContentIfNotHandled()?.let { m -> showToast(m) }
            })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(CONFIGURATION_CHANGE, true)
    }
}
