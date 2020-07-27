package com.zywczas.bestonscreen.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.databinding.ActivityDetailsBinding
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.EXTRA_MOVIE
import com.zywczas.bestonscreen.utilities.logD
import com.zywczas.bestonscreen.utilities.showToast
import com.zywczas.bestonscreen.viewmodels.DetailsVM
import com.zywczas.bestonscreen.viewmodels.factories.DetailsVMFactory
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {

    lateinit var viewModel: DetailsVM
    @Inject
    lateinit var factory: DetailsVMFactory
    @Inject
    lateinit var picasso: Picasso
    lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startDetailsActivitySetupChain()
        setupPosterImage()
    }

    private fun startDetailsActivitySetupChain(){
        injectDependencies{ success ->
            if (success) {
                getViewModelAndIntent { success2 ->
                    if (success2) {
                        setupDataBinding()
                        setupAddToListBtnStateObserver()
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
        return if (movieFromParcel != null) {
            movie = movieFromParcel
            viewModel = ViewModelProvider(this, factory).get(DetailsVM::class.java)
            complete(true)
        } else {
            showToast("Cannot load the movie.")
            logD("Cannot get movie from parcel in ${this.localClassName}")
            complete(false)
        }
    }

    private fun setupDataBinding() {
        val binding : ActivityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        binding.viewModel = viewModel
        binding.movie = movie
    }

    private fun setupAddToListBtnStateObserver() {
        viewModel.isMovieInDb(movie.id).observe(this,
            Observer {isInDb ->
                addToListBtn.isChecked = isInDb
                addToListBtn.tag = isInDb
            })

    }

    private fun setupPosterImage() {
        val posterPath = "https://image.tmdb.org/t/p/w300" + movie.posterPath
        picasso.load(posterPath)
            .resize(250, 0)
            .error(R.drawable.error_image)
            .into(posterImageViewDetails)
    }

    fun addToListClicked(view: View) {
        val isButtonChecked = addToListBtn.tag as Boolean
        viewModel.addOrDeleteMovie(movie, isButtonChecked)
            .observe(this, Observer {
                it.getContentIfNotHandled()?.let { m -> showToast(m) }
            })
    }

    override fun onDestroy() {
        viewModel.clearDisposables()
        super.onDestroy()
    }
}
