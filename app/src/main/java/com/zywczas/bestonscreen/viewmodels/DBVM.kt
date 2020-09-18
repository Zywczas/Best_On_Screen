package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.zywczas.bestonscreen.model.DBRepository

class DBVM(repo: DBRepository) : ViewModel() {

    val moviesLD =
        LiveDataReactiveStreams.fromPublisher(repo.getMoviesFromDB())

}