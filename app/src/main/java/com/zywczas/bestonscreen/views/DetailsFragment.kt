package com.zywczas.bestonscreen.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.*
import com.zywczas.bestonscreen.viewmodels.DetailsVM
import com.zywczas.bestonscreen.viewmodels.factories.DetailsVMFactory
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

class DetailsFragment @Inject constructor(
    private val viewModelFactory: DetailsVMFactory,
    private val picasso: Picasso,
    private val networkCheck: NetworkCheck
) : Fragment() {

    private val viewModel: DetailsVM by viewModels { viewModelFactory }
    private val movie : Movie
            by lazyAndroid { requireArguments().let { DetailsFragmentArgs.fromBundle(it).movie } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUIState()
        setupMessageObserver()
        checkInternetConnection()
        setupOnClickListener()
    }

//todo dac jakis lepsze uzycie tekstu zeby nie trzeba bylo kominikatu zagluszac
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
        genresTextViewDetails.text = movie.genresDescription
        setupAddToListBtnStateObserver()
    }

    private fun setupAddToListBtnStateObserver() {
        viewModel.isMovieInDbLD.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isInDb ->
                addToListBtn.isChecked = isInDb
                addToListBtn.tag = isInDb
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
        if (!networkCheck.isNetworkConnected) {
            showToast(CONNECTION_PROBLEM)
        }
    }

    private fun setupOnClickListener() {
        addToListBtn.setOnClickListener {
            val isButtonChecked = addToListBtn.tag as Boolean
            viewModel.addOrDeleteMovie(movie, isButtonChecked)
            updateAddToListBtnState()
        }
    }

}