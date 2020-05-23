package com.zywczas.bestonscreen.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.utilities.API_KEY
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList


class Repository private constructor() {

    companion object {
        @Volatile private var instance: Repository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Repository().also { instance = it }
        }
    }

    var moviesLiveData = MutableLiveData<List<Movie>>()
    val compositeDisposable = CompositeDisposable()
    val movies = ArrayList<Movie>()


    //trzeba wziac obiekt retrofit stad i wrzucic w daggera, albo caly service
    val tmdbService = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDBService::class.java)

    fun getPopularMoviesLiveData (context: Context) : MutableLiveData<List<Movie>> {
        val tmdbObservable = tmdbService.getPopularMovies(API_KEY)
        compositeDisposable.add( tmdbObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { moviesApiResponse -> Observable.fromArray(*moviesApiResponse.results!!.toTypedArray()) }
            .subscribeWith(object : DisposableObserver<Movie>(){
                override fun onComplete() {
                    moviesLiveData.postValue(movies)
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

    fun clear() = compositeDisposable.clear()

}