package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.DBRepository
import javax.inject.Inject

class DBVM @Inject constructor(repo: DBRepository) : ViewModel() {

    val moviesLD
            by lazy { LiveDataReactiveStreams.fromPublisher(repo.getMoviesFromDB()) }

}