package com.zywczas.bestonscreen.model


import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.utilities.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiMoviesRepo @Inject constructor(
    private val compositeDisposables: CompositeDisposable,
    private val movies: ArrayList<Movie>,
    private val apiService: ApiService,
    private val moviesLd : MutableLiveData<Triple<List<Movie>, Int, String>>
) {
    //todo poprawic te funkcje i usunac komentarze
    private var currentPage = 1
    //just any number >= 1 at the beginning
    private var lastPage = 1

    fun clearDisposables() = compositeDisposables.clear()

    //todo poprawic te funkcje i usunac komentarze
    fun getMoviesFromApi (category: String, page: Int) : MutableLiveData<Triple<List<Movie>, Int, String>> {
        //if new category, then reset the list
        if (page == 1 ) {
            movies.clear()
        }

        if (page > lastPage) {
            //sends 0 as a flag to Observer
            moviesLd.postValue(Triple(movies, 0, category))
            return  moviesLd
        }

        val moviesObservableApi = when (category) {
            POPULAR -> { apiService.getPopularMovies(API_KEY, page) }
            TOP_RATED -> { apiService.getTopRatedMovies(API_KEY, page) }
            UPCOMING -> { apiService.getUpcomingMovies(API_KEY, page) }
            //this option sends empty LiveEvent just to remove observers
            EMPTY_CATEGORY -> { movies.clear()
                moviesLd.postValue(Triple(movies, currentPage, category))
                return  moviesLd }
            else -> { movies.clear()
                logD("incorrect movie category passed to 'getMoviesFromApi'")
                moviesLd.postValue(Triple(movies, currentPage, category))
                return  moviesLd}
        }

        compositeDisposables.add(moviesObservableApi
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .flatMap { movieApiResponse ->
                movieApiResponse.page?.let { currentPage = it }
                movieApiResponse.totalPages?.let { lastPage = it }
                Observable.fromArray(*movieApiResponse.movies!!.toTypedArray()) }

            .flatMap { movieFromApi ->
                movieFromApi.genreIds?.let { movieFromApi.transferGenresListToVariables(it) }
                Observable.just( toMovie(movieFromApi) )
            }
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onComplete() {
                    moviesLd.postValue(Triple(movies, currentPage, category))
                }

                override fun onNext(m: Movie?) {
                    if (m != null) {
                        movies.add(m)
                    }
                }

                override fun onError(e: Throwable?) {
                     logD(e)
                }
            })
        )
        return moviesLd
    }

}
