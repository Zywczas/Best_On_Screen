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

        App.moviesComponent.inject(this)
        viewModel = ViewModelProvider(this, factory).get(DetailsVM::class.java)
        movieFromParcel = intent.getParcelableExtra(EXTRA_MOVIE)!!
        val binding : ActivityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        binding.viewModel = viewModel
        binding.movie = movieFromParcel

        setupPosterImage()
        checkIfMovieIsInDb()
    }

    private fun setupPosterImage() {
        val posterPath = "https://image.tmdb.org/t/p/w300" + movieFromParcel.posterPath

        picasso.load(posterPath)
            .resize(250, 0)
            .error(R.drawable.error_image)
            .into(posterImageViewDetails)
    }
//todo poprawic
    private fun checkIfMovieIsInDb() {
        viewModel.checkIfMovieIsInDb(movieFromParcel.id!!).observe(this,
            Observer {boolean ->
                addToListBtn.isChecked = boolean
                //this tag is used in addToListClicked() so it knows whether to add or delete movie
                addToListBtn.tag = boolean.toString()
            })
    }

    fun addToListClicked(view: View) {
        //todo tutaj podajemy true lub false, tzn ze funkcja wykonuje 2 rzeczy a powinna tylko 1 - moze zamienic na chooseAddOrDelete
        viewModel.addDeleteMovie(movieFromParcel, addToListBtn.tag.toString())
            .observe(this, Observer {
                it.getContentIfNotHandled()?.let { m -> showToast(m) }
            })
    }

    override fun onDestroy() {
        viewModel.clearDisposables()
        super.onDestroy()
    }
}
