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
    lateinit var movieFromParcel: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startDetailsActivitySetupChain()
        setupPosterImage()
    }

    private fun startDetailsActivitySetupChain(){
        val areDependenciesInjected = injectDependenciesAndConfirmFinish()

        if (areDependenciesInjected) {
            setupLevel1()
        }
    }

    private fun injectDependenciesAndConfirmFinish() : Boolean {
        App.moviesComponent.inject(this)
        return true
    }

    private fun setupLevel1() {
        val areViewModelAndIntentSetup = getViewModelAndIntentAndConfirmFinish()

        if(areViewModelAndIntentSetup) {
            setupDataBinding()
            setupAddToListBtnState()
        }
    }

    private fun getViewModelAndIntentAndConfirmFinish() : Boolean {
        viewModel = ViewModelProvider(this, factory).get(DetailsVM::class.java)
        intent.getParcelableExtra<Movie>(EXTRA_MOVIE)?.let { movieFromParcel = it }
        return true
    }

    private fun setupDataBinding() {
        val binding : ActivityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        binding.viewModel = viewModel
        binding.movie = movieFromParcel
    }

//todo pomyslec nad zmiana tych nulli
    private fun setupAddToListBtnState() {
        movieFromParcel.id?.let {
            viewModel.isMovieInDb(it).observe(this,
                Observer {isInDb ->
                    addToListBtn.isChecked = isInDb
                    addToListBtn.tag = isInDb
                })
        }
    }

    private fun setupPosterImage() {
        val posterPath = "https://image.tmdb.org/t/p/w300" + movieFromParcel.posterPath

        picasso.load(posterPath)
            .resize(250, 0)
            .error(R.drawable.error_image)
            .into(posterImageViewDetails)
    }

    fun addToListClicked(view: View) {
        val isButtonChecked = addToListBtn.tag as Boolean

        viewModel.addOrDeleteMovie(movieFromParcel, isButtonChecked)
            .observe(this, Observer {
                it.getContentIfNotHandled()?.let { m -> showToast(m) }
            })
    }

    override fun onDestroy() {
        viewModel.clearDisposables()
        super.onDestroy()
    }
}
