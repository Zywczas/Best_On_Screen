package com.zywczas.bestonscreen.model

import androidx.lifecycle.LiveData
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.db.MovieFromDB
import com.zywczas.bestonscreen.utilities.LiveEvent
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.utilities.logD
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DBRepository @Inject constructor(
    private val movies: ArrayList<Movie>,
    private val movieDao: MovieDao
) {

    fun getMoviesFromDB(): Flowable<Resource<List<Movie>>> {
        val databaseFlowable = RxJavaBridge.toV3Flowable(movieDao.getMovies())
        return databaseFlowable
            .subscribeOn(Schedulers.io())
            .onBackpressureBuffer()
            .map { moviesFromDB -> convertToMovies(moviesFromDB)
               Resource.success(movies.toList())
            }
            .onErrorReturn { Resource.error("Cannot access movies from the data base.", null) }
    }

    private fun convertToMovies(moviesFromDB: List<MovieFromDB>) {
        movies.clear()
        for (e in moviesFromDB) {
            movies.add(toMovie(e))
        }
    }

}