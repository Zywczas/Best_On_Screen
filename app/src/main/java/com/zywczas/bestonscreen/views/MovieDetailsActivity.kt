package com.zywczas.bestonscreen.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.databinding.ActivityMovieDetailsBinding
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.EXTRA_MOVIE
import com.zywczas.bestonscreen.utilities.showToast
import com.zywczas.bestonscreen.viewmodels.MovieDetailsVM
import com.zywczas.bestonscreen.viewmodels.factories.MovieDetailsVMFactory
import kotlinx.android.synthetic.main.activity_movie_details.*
import javax.inject.Inject

class MovieDetailsActivity : AppCompatActivity() {

    lateinit var viewModel: MovieDetailsVM
    @Inject
    lateinit var factory: MovieDetailsVMFactory
    @Inject
    lateinit var picasso: Picasso
    lateinit var movieFromParcel: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.moviesComponent.inject(this)
        viewModel = ViewModelProvider(this, factory).get(MovieDetailsVM::class.java)
        movieFromParcel = intent.getParcelableExtra(EXTRA_MOVIE)!!
        val binding : ActivityMovieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)
        binding.viewModel = viewModel
        binding.movie = movieFromParcel

        setupPosterImage()
        checkIfMovieIsInDb()
    }
    //todo zmienic nazwe na guziku z Add to your list na add to my list i categorie tez

    @SuppressLint("SetTextI18n")
    private fun setupPosterImage() {
        val posterPath = "https://image.tmdb.org/t/p/w300" + movieFromParcel.posterPath

        picasso.load(posterPath)
            .resize(250, 0)
            .error(R.drawable.error_image)
            .into(posterImageViewDetails)
    }

    fun addToListClicked(view: View) {
        viewModel.addDeleteMovie(movieFromParcel, addToListBtn.tag.toString())
            .observe(this, Observer {
                it.getContentIfNotHandled()?.let { m -> showToast(m) }
            })
    }

    private fun checkIfMovieIsInDb() {
        viewModel.checkIfMovieIsInDb(movieFromParcel.id!!).observe(this,
            Observer {boolean ->
                    addToListBtn.isChecked = boolean
                    //this tag is used in addToListClicked() so it knows whether to add or delete movie
                    addToListBtn.tag = boolean.toString()
            })
    }

    override fun onDestroy() {
        viewModel.clearDisposables()
        super.onDestroy()
    }
}
