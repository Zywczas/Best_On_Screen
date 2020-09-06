package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.utilities.Event
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepository @Inject constructor(
    private val movieDao: MovieDao
){

    fun checkIfMovieIsInDB (movieId: Int) : Flowable<Boolean> {
        val isMovieInDbFlowable = RxJavaBridge.toV3Flowable(movieDao.getIdCount(movieId))
        return isMovieInDbFlowable
            .subscribeOn(Schedulers.io())
            .map{ idCount -> toBoolean(idCount) }
    }

    private fun toBoolean(idCount: Int) : Boolean {
        return when(idCount) {
            0 -> false
            else -> true
        }
    }

    fun addMovieToDB (movie: Movie) : Flowable<Event<String>> {
        val single = RxJavaBridge.toV3Single(movieDao.insertMovie(toMovieFromDB(movie)))

        return single
            .subscribeOn(Schedulers.io())
            .flatMapPublisher {rowId ->
                if (rowId > 0) {
                    Flowable.just(Event("Movie added to your list"))
                } else {
                    Flowable.just(Event("Cannot add the movie. Try again."))
                }
                //todo to dac resource z podzieleniem na rozne przypadki rowId

            }
    }

    fun deleteMovieFromDB(movie : Movie) :  Flowable<Event<String>> {
        val single = RxJavaBridge.toV3Single(movieDao.deleteMovie(toMovieFromDB(movie)))
        return single
            .subscribeOn(Schedulers.io())
            .flatMapPublisher { numberOfRowsRemoved ->
                if (numberOfRowsRemoved > 0 ){
                    Flowable.just(Event("Movie removed from your list"))
                } else {
                    Flowable.just(Event("Cannot remove the movie. Try again."))
                }
            //todo to dac resource z podzieleniem na rozne przypadki rowId

        }
    }

}