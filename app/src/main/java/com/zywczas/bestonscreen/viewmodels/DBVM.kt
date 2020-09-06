package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.DBRepository
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Event
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

//todo pousuwac savedstatehandle
class DBVM(
    repo: DBRepository,
    //not used yet but implemented for future expansion
    private val handle: SavedStateHandle
) : ViewModel() {

    val moviesLD =
        LiveDataReactiveStreams.fromPublisher(repo.getMoviesFromDB())

}