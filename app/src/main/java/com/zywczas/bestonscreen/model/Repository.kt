package com.zywczas.bestonscreen.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.utilities.API_KEY
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val compositeDisposable: CompositeDisposable,
    private val movies: ArrayList<Movie>,
    private val moviesLiveData: MutableLiveData<List<Movie>>,
    private val tmdbService: TMDBService) {

    lateinit var moviesObservable: Observable<MovieApiResponse>

    fun clear() = compositeDisposable.clear()

    fun getMoviesLiveData (context: Context, movieCategory: MovieCategory) : MutableLiveData<List<Movie>> {
        movies.clear()

        when (movieCategory) {
            MovieCategory.POPULAR -> {moviesObservable = tmdbService.getPopularMovies(API_KEY)}
            MovieCategory.TOP_RATED -> {moviesObservable = tmdbService.getTopRatedMovies(API_KEY)}
            MovieCategory.UPCOMING -> {moviesObservable = tmdbService.getUpcomingMovies(API_KEY)}
        }

        compositeDisposable.add( moviesObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { movieApiResponse -> Observable.fromArray(*movieApiResponse.movies!!.toTypedArray()) }
            .subscribeWith(object : DisposableObserver<Movie>(){
                override fun onComplete() {
                    moviesLiveData.postValue(movies)
                    Toast.makeText(context, "Coming soon", Toast.LENGTH_LONG).show()
                }
                override fun onNext(m: Movie?) {
                    if (m != null) {
                        movies.add(m)
                    }
                }
                override fun onError(e: Throwable?) {
                    Toast.makeText(context, "Problem with downloading movies", Toast.LENGTH_LONG).show()
                    Log.d("ERROR", "${e?.localizedMessage}")
                }
            })
        )
        return moviesLiveData
    }

//    fun getUpcomingMoviesLiveData (context: Context) : MutableLiveData<List<Movie>> {
//        val upcomingMoviesObservable = tmdbService.getUpcomingMovies(API_KEY)
//        movies.clear()
//
//        compositeDisposable.add(
//            upcomingMoviesObservable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap { movieApiResponse -> Observable.fromArray(*movieApiResponse.movies!!.toTypedArray()) }
//                .subscribeWith(object : DisposableObserver<Movie>() {
//                    override fun onComplete() {
//                        moviesLiveData.postValue(movies)
//
//                    }
//
//                    override fun onNext(m: Movie?) {
//                        if (m != null) {
//                            movies.add(m)
//                        }
//                    }
//
//                    override fun onError(e: Throwable?) {
//                        Toast.makeText(context, "Problem with downloading movies", Toast.LENGTH_LONG).show()
//                        Log.d("ERROR", "${e?.localizedMessage}")
//                    }
//
//                })
//        )
//        return moviesLiveData
//    }



}