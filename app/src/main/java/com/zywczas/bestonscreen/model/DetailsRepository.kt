package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.utilities.Event
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

open class DetailsRepository @Inject constructor(private val movieDao: MovieDao) {

    private val addSuccess by lazy { "Movie added to your list." }
    private val addError by lazy { "Cannot add the movie. Close the app. Try again." }
    private val deleteSuccess by lazy { "Movie removed from your list." }
    private val deleteError by lazy { "Cannot remove the movie. Close the app. Try again." }

    open fun checkIfMovieIsInDB(movieId: Int): Flowable<Event<Boolean>> {
        val isMovieInDbFlowable = RxJavaBridge.toV3Flowable(movieDao.getIdCount(movieId))
        return isMovieInDbFlowable
            .subscribeOn(Schedulers.io())
            .map { idCount -> Event(toBoolean(idCount)) }
    }

    private fun toBoolean(idCount: Int): Boolean {
        return when (idCount) {
            0 -> false
            else -> true
        }
    }

    open fun addMovieToDB(movie: Movie): Flowable<Event<String>> {
        val single = RxJavaBridge.toV3Single(movieDao.insertMovie(toMovieFromDB(movie)))
        return single
            .subscribeOn(Schedulers.io())
            .flatMapPublisher { rowId ->
                if (rowId > 0) {
                    Flowable.just(Event(addSuccess))
                } else {
                    Flowable.just(Event(addError))
                }
            }
            .onErrorReturn { Event(addError) }
    }

    open fun deleteMovieFromDB(movie: Movie): Flowable<Event<String>> {
        val single = RxJavaBridge.toV3Single(movieDao.deleteMovie(toMovieFromDB(movie)))
        return single
            .subscribeOn(Schedulers.io())
            .flatMapPublisher { numberOfRowsRemoved ->
                if (numberOfRowsRemoved > 0) {
                    Flowable.just(Event(deleteSuccess))
                } else {
                    Flowable.just(Event(deleteError))
                }
            }
            .onErrorReturn { Event(deleteError) }
    }

}