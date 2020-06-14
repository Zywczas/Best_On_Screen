package com.zywczas.bestonscreen.model

import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.webservice.TMDBService
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.logD
import com.zywczas.bestonscreen.utilities.toMovieFromDB
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailsRepo @Inject constructor(
    private val compositeDisposables: CompositeDisposable,
    private val movieDao: MovieDao,
    private val booleanEventLd: MutableLiveData<Event<Boolean>>,
    val stringEventLd : MutableLiveData<Event<String>>
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
                        stringEventLd.postValue(Event("Movie added to your list"))
                    }
                    override fun onError(e: Throwable?) {
                        logD(e)
                        stringEventLd.postValue(Event("Problem with adding the movie"))
                    }
                })
        )
        return stringEventLd
    }
//zamienic wszedzie Event na LiveEvent
    fun checkIfMovieIsInDB (movieId: Int) : MutableLiveData<Event<Boolean>> {

        val movieFromDBObservable = RxJavaBridge.toV3Observable(movieDao.checkIfExists(movieId))
        compositeDisposables.add(
            movieFromDBObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({when(it){
                    1 -> booleanEventLd.postValue(Event(true))
                    0 -> booleanEventLd.postValue(Event(false))
                }}, { logD(it) }
                )
        )
        return booleanEventLd
    }

    fun deleteMovieFromDB(movie : Movie) :  MutableLiveData<Event<String>> {
        val completableRx3 = RxJavaBridge.toV3Completable(
            movieDao.deleteMovie(toMovieFromDB(movie))
        )

        compositeDisposables.add(
            completableRx3
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver(){
                    override fun onComplete() {
                        stringEventLd.postValue(Event("Movie deleted from your list"))
                    }

                    override fun onError(e: Throwable?) {
                        stringEventLd.postValue(Event("Problem with deleting the movie"))
                        logD(e)
                    }

                })
        )
        return stringEventLd
    }

}