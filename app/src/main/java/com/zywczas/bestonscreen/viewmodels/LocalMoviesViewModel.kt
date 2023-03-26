package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.repositories.LocalMoviesRepository
import javax.inject.Inject

class LocalMoviesViewModel @Inject constructor(repo: LocalMoviesRepository) : ViewModel() {

    val movies = LiveDataReactiveStreams.fromPublisher(repo.getMoviesFromDB())

}
