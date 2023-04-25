package com.zywczas.bestonscreen.model.repositories

import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.toMovie
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LocalMoviesRepository @Inject constructor(private val movieDao: MovieDao) {

    fun getMoviesFromDB(): Flowable<List<Movie>> = RxJavaBridge.toV3Flowable(movieDao.getMovies())
        .subscribeOn(Schedulers.io())
        .onBackpressureBuffer()
        .map { moviesFromDB -> moviesFromDB.map { toMovie(it) } }
}
