package com.zywczas.bestonscreen.model

import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.LiveEvent
import com.zywczas.bestonscreen.utilities.logD
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepository @Inject constructor(
    private val compositeDisposables: CompositeDisposable,
    private val movieDao: MovieDao,
    private val booleanLiveEvent: LiveEvent<Boolean>,
    private val stringEventLiveData : MutableLiveData<Event<String>>
){
    fun clearDisposables() = compositeDisposables.clear()

    fun checkIfMovieIsInDB (movieId: Int) : LiveEvent<Boolean> {
        val movieFromDBObservable = RxJavaBridge.toV3Observable(movieDao.getIdCount(movieId))
        compositeDisposables.add(
            movieFromDBObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({idCount -> updateBooleanLiveEvent(idCount)
                }, { updateBooleanLiveEventWithFalse()
                    logD(it) }
                )
        )
        return booleanLiveEvent
    }

    private fun updateBooleanLiveEvent(idCount: Int) {
        when(idCount) {
            0 -> booleanLiveEvent.postValue(false)
            else -> booleanLiveEvent.postValue(true)
        }
    }

    private fun updateBooleanLiveEventWithFalse() {
        booleanLiveEvent.postValue(false)
    }

    fun addMovieToDB (movie: Movie) : MutableLiveData<Event<String>> {
        val completable = RxJavaBridge.toV3Completable(
            movieDao.addMovie(toMovieFromDB(movie))
        )
        compositeDisposables.add(
            completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver(){
                    override fun onComplete() {
                        updateStringEventLiveData("Movie added to your list")
                    }

                    override fun onError(e: Throwable?) {
                        logD(e)
                        updateStringEventLiveData("Problem with adding the movie")
                    }
                })
        )
        return stringEventLiveData
    }

    private fun updateStringEventLiveData(value: String) {
        stringEventLiveData.postValue(Event(value))
    }

    fun deleteMovieFromDB(movie : Movie) :  MutableLiveData<Event<String>> {
        val completable = RxJavaBridge.toV3Completable(
            movieDao.deleteMovie(toMovieFromDB(movie))
        )
        compositeDisposables.add(
            completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver(){
                    override fun onComplete() {
                        updateStringEventLiveData("Movie deleted from your list")
                    }

                    override fun onError(e: Throwable?) {
                        updateStringEventLiveData("Problem with deleting the movie")
                        logD(e)
                    }
                })
        )
        return stringEventLiveData
    }

}