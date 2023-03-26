package com.zywczas.bestonscreen.model.repositories

import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.db.LocalMovie
import com.zywczas.bestonscreen.model.toMovie
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

open class LocalMoviesRepository @Inject constructor(private val movieDao: MovieDao) {

    private val movies by lazy { mutableListOf<Movie>() }

    open fun getMoviesFromDB(): Flowable<List<Movie>> {
        val databaseFlowable = RxJavaBridge.toV3Flowable(movieDao.getMovies())
        return databaseFlowable
            .subscribeOn(Schedulers.io())
            .onBackpressureBuffer()
            .map { moviesFromDB ->
                convertToMovies(moviesFromDB)
                movies
            }
    }

    private fun convertToMovies(moviesFromDB: List<LocalMovie>) {
        movies.clear()
        for (e in moviesFromDB) {
            movies.add(toMovie(e))
        }
    }

}
