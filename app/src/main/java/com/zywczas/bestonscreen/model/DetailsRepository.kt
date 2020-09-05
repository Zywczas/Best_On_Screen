package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.utilities.Event
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepository @Inject constructor(
    private val movieDao: MovieDao
){

    fun checkIfMovieIsInDB (movieId: Int) : Flowable<Boolean> {
        val isMovieInDbObservable = RxJavaBridge.toV3Flowable(movieDao.getIdCount(movieId))
        return isMovieInDbObservable
            .subscribeOn(Schedulers.io())
            .map{ idCount -> toBoolean(idCount) }
    }

    private fun toBoolean(idCount: Int) : Boolean {
        return when(idCount) {
            0 -> false
            else -> true
        }
    }

//    fun addMovieToDB (movie: Movie) : MutableLiveData<Event<String>> {
//        val completable = RxJavaBridge.toV3Completable(
//            movieDao.addMovie(toMovieFromDB(movie))
//        )
//
//            completable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : DisposableCompletableObserver(){
//                    override fun onComplete() {
//                        updateStringEventLiveData("Movie added to your list")
//                    }
//
//                    override fun onError(e: Throwable?) {
//                        logD(e)
//                        updateStringEventLiveData("Problem with adding the movie")
//                    }
//                })
//
//        return stringEventLiveData
//    }

    fun addMovieToDB (movie: Movie) : Flowable<Event<String>> {
        val single = movieDao.insertMovie(toMovieFromDB(movie))
        return single
            .subscribeOn(Schedulers.io())
            .onErrorReturn { Event("Cannot add the movie to your list") }
            .doOnSuccess { Event("Movie Added to your list") }
            .toFlowable()
    }

    fun deleteMovieFromDB(movie : Movie) :  Flowable<Event<String>> {
        val single = movieDao.deleteMovie(toMovieFromDB(movie))
        return single
            .subscribeOn(Schedulers.io())
            .onErrorReturn { Event("Cannot delete the movie.") }
            .doOnSuccess { Event("Movie removed from your list") }
            .toFlowable()
    }

}