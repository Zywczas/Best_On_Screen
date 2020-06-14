package com.zywczas.bestonscreen.model

import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.webservice.TMDBService
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.logD
import com.zywczas.bestonscreen.utilities.toMovie
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DBMoviesRepo @Inject constructor(
    private val compositeDisposables: CompositeDisposable,
    private val movies: ArrayList<Movie>,
    private val moviesEventLd: MutableLiveData<Event<List<Movie>>>,
    private val movieDao: MovieDao
) {
    fun clearDisposables() = compositeDisposables.clear()

    fun getMoviesFromDB () : MutableLiveData<Event<List<Movie>>> {
        val moviesObservableDB = RxJavaBridge.toV3Flowable(movieDao.getMovies())

        compositeDisposables.add(
            moviesObservableDB
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                //converts MovieFromDB to list of general Movie class
                .map { moviesFromDB ->
                    movies.clear()
                    for (e in moviesFromDB) {
                        movies.add(toMovie(e))
                    }
                    movies
                }
                //Consumer onNext & onError
                    //zamienic Event na LiveEvent!!!!!!!!!!!!!!!!!!!!!!!!
                .subscribe({ listOfMovies ->  moviesEventLd.postValue(Event(listOfMovies))
                    logD("wysyla liste z DB")
                }, { logD(it) }
                )
        )
        return moviesEventLd
    }
}