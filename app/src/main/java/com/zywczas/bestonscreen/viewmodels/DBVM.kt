package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.*
import com.zywczas.bestonscreen.model.DBRepository
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.Event
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

//todo pousuwac savedstatehandle
class DBVM (
    repo: DBRepository,
    //todo dac pozniej private
    val moviesMutableLD: MutableLiveData<List<Movie>>,
    private val errorMLD: MutableLiveData<Event<String>>,
    //not used yet but implemented for future expansion
    private val handle: SavedStateHandle
) : ViewModel() {

//todo nie obsluguje errors, dac ta specjalna klase
    val moviesLD = LiveDataReactiveStreams.fromPublisher(
        repo.getMoviesFromDB()
            .doOnError { errorMLD.postValue(Event("Problem with accessing your movies")) }
    )
    //todo dac orientation change handling

    val errorLD = errorMLD as LiveData<Event<String>>

}