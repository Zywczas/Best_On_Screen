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
    val stringEventLiveData : MutableLiveData<Event<String>>
){
    fun clearDisposables() = compositeDisposables.clear()

    fun addMovieToDB (movie: Movie) : MutableLiveData<Event<String>> {
        val completableRx3 = RxJavaBridge.toV3Completable(
            movieDao.addMovie(toMovieFromDB(movie))
        )
        compositeDisposables.add(
            completableRx3
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver(){
                    override fun onComplete() {
                        stringEventLiveData.postValue(Event("Movie added to your list"))
                    }
                    override fun onError(e: Throwable?) {
                        logD(e)
                        stringEventLiveData.postValue(Event("Problem with adding the movie"))
                    }
                })
        )
        return stringEventLiveData
    }

    fun checkIfMovieIsInDB (movieId: Int) : LiveEvent<Boolean> {
        val movieFromDBObservable = RxJavaBridge.toV3Observable(movieDao.checkIfIsInDB(movieId))
        compositeDisposables.add(
            movieFromDBObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({when(it){
                    //todo zamienic na true i false
                    1 -> booleanLiveEvent.postValue(true)
                    0 -> booleanLiveEvent.postValue(false)
                }}, { logD(it) }
                )
        )
        return booleanLiveEvent
    }

    fun deleteMovieFromDB(movie : Movie) :  MutableLiveData<Event<String>> {
        val completableRx3 = RxJavaBridge.toV3Completable(
            movieDao.deleteMovie(
                toMovieFromDB(
                    movie
                )
            )
        )

        compositeDisposables.add(
            completableRx3
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver(){
                    override fun onComplete() {
                        stringEventLiveData.postValue(Event("Movie deleted from your list"))
                    }

                    override fun onError(e: Throwable?) {
                        stringEventLiveData.postValue(Event("Problem with deleting the movie"))
                        logD(e)
                    }

                })
        )
        return stringEventLiveData
    }

}