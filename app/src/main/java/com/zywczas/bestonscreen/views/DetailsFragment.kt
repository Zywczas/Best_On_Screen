package com.zywczas.bestonscreen.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.utilities.CONNECTION_PROBLEM
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.utilities.lazyAndroid
import com.zywczas.bestonscreen.utilities.showToast
import com.zywczas.bestonscreen.viewmodels.DetailsVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

class DetailsFragment @Inject constructor(
    private val viewModelFactory: ViewModelsProviderFactory,
    private val picasso: Picasso,
    private val networkCheck: NetworkCheck
) : Fragment(R.layout.fragment_details) {

    private val viewModel: DetailsVM by viewModels { viewModelFactory }
    private val movie
            by lazyAndroid { requireArguments().let { DetailsFragmentArgs.fromBundle(it).movie } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUIState()
        setupMessageObserver()
        checkInternetConnection()
        setupOnClickListener()
    }

    private fun setupUIState() {
        val posterPath = "https://image.tmdb.org/t/p/w300" + movie.posterPath
        picasso.load(posterPath)
            .resize(250, 0)
            .error(R.drawable.error_image)
            .into(posterImageViewDetails)
        titleTextViewDetails.text = movie.title
        val rate = "Rate: ${movie.voteAverage}"
        rateTextViewDetails.text = rate
        val releaseDate = "Release date: ${movie.releaseDate}"
        releaseDateTextViewDetails.text = releaseDate
        overviewTextViewDetails.text = movie.overview
        genresTextViewDetails.text = movie.genresDescription
        setupAddToMyListBtnStateObserver()
    }

    private fun setupAddToMyListBtnStateObserver() {
        viewModel.isMovieInDbLD.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isInDb ->
                addToMyListBtnDetails.isChecked = isInDb
                addToMyListBtnDetails.tag = isInDb
            }
        }
        updateAddToListBtnState()
    }

    private fun updateAddToListBtnState() {
        viewModel.checkIfIsInDb(movie.id)
    }

    private fun setupMessageObserver() {
        viewModel.messageLD.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                showToast(message)
            }
        }
    }

    private fun checkInternetConnection() {
        if (networkCheck.isConnected.not()) {
            showToast(CONNECTION_PROBLEM)
        }
    }

    private fun setupOnClickListener() {
        addToMyListBtnDetails.setOnClickListener {
            val isButtonChecked = addToMyListBtnDetails.tag as Boolean
            viewModel.addOrDeleteMovie(movie, isButtonChecked)
            updateAddToListBtnState()
        }
    }

}