package com.zywczas.bestonscreen.model

import androidx.lifecycle.LiveData
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.db.MovieFromDB
import com.zywczas.bestonscreen.utilities.LiveEvent
import com.zywczas.bestonscreen.utilities.logD
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DBRepository @Inject constructor(
    private val compositeDisposables: CompositeDisposable,
    private val movies: ArrayList<Movie>,
    private val moviesLiveEvent: LiveEvent<List<Movie>>,
    private val messageLiveEvent: LiveEvent<String>,
    private val movieDao: MovieDao
) {
    fun clearDisposables() = compositeDisposables.clear()

    fun getErrorLiveEvent() = messageLiveEvent as LiveData<String>

    fun getMoviesFromDB () : LiveEvent<List<Movie>> {
        val databaseFlowable = RxJavaBridge.toV3Flowable(movieDao.getMovies())

        compositeDisposables.add(
            databaseFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .map { moviesFromDB -> convertToMovies(moviesFromDB) }
                .subscribe({ movies -> updateLiveEvent(movies)
                }, { logD(it)
                sendErrorMessage()}
                )
        )
        return moviesLiveEvent
    }

    private fun convertToMovies(moviesFromDB: List<MovieFromDB>) : List<Movie> {
        movies.clear()
        for (e in moviesFromDB) {
            movies.add(toMovie(e))
        }
        return movies
    }

    private fun updateLiveEvent(movies: List<Movie>) {
        moviesLiveEvent.postValue(movies)
    }

    private fun sendErrorMessage() {
        messageLiveEvent.postValue("Problem with accessing movies from your list")
    }

}