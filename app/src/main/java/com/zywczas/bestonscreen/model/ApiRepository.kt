package com.zywczas.bestonscreen.model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.utilities.ERROR_FLAG
import com.zywczas.bestonscreen.utilities.NO_MORE_PAGES_FLAG
import com.zywczas.bestonscreen.utilities.logD
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(
    private val compositeDisposables: CompositeDisposable,
    private val movies: ArrayList<Movie>,
    private val apiService: ApiService,
    private val moviesLiveData : MutableLiveData<Triple<List<Movie>, Int, Category>>
) {

    private val apiKey = "43a74b6228b35b23e401df1c6a464af1"
    private val firstPageOfNewCategory = 1
    private var currentPage = firstPageOfNewCategory
    private var lastPageOfCategory = firstPageOfNewCategory
    private var nextPage = 1
    private lateinit var category: Category

    fun clearDisposables() = compositeDisposables.clear()

    fun getLiveData() = moviesLiveData as LiveData<Triple<List<Movie>, Int, Category>>

    fun getMoviesFromApi (nextCategory: Category, nextPage: Int) : MutableLiveData<Triple<List<Movie>, Int, Category>> {
        this.category = nextCategory
        this.nextPage = nextPage

        return if (nextPage > lastPageOfCategory) {
            sendLastPageFlag()
        } else {
            downloadAndSendMovies()
        }
    }

    private fun sendLastPageFlag() : MutableLiveData<Triple<List<Movie>, Int, Category>> {
        val anyCategory = Category.POPULAR
        moviesLiveData.postValue(Triple(movies, NO_MORE_PAGES_FLAG, anyCategory))
        return moviesLiveData
    }

    private fun downloadAndSendMovies() : MutableLiveData<Triple<List<Movie>, Int, Category>> {
        if (nextPage == firstPageOfNewCategory ) {
            resetDownloadedMovies()
        }
        downloadMoviesToLiveData()
        return moviesLiveData
    }

    private fun resetDownloadedMovies() = movies.clear()

    private fun downloadMoviesToLiveData(){
        val apiObservable = setupApiObservable()

        compositeDisposables.add(apiObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { apiResponse ->
                apiResponse.page?.let { currentPage = it }
                apiResponse.totalPages?.let { lastPageOfCategory = it }
                apiResponse.movies?.let { Observable.fromArray(*it.toTypedArray()) }
            }
            .flatMap { movieFromApi ->
                movieFromApi.genreIds?.let { movieFromApi.assignGenresListToVariables(it) }
                Observable.just( toMovie(movieFromApi) )
            }
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onNext(m: Movie?) {
                    if (m != null) {
                        getDownloadedMovie(m)
                    }
                }

                override fun onComplete() {
                    updateLiveData()
                }
//todo sprawdzic czy da sie tutaj dac exception zamiast kod bledu
                override fun onError(e: Throwable?) {
                    logD(e)
                    updateLiveDataWithError()
                }
            })
        )
    }

    private fun setupApiObservable() = when (category) {
        Category.POPULAR -> { apiService.getPopularMovies(apiKey, nextPage) }
        Category.TOP_RATED -> { apiService.getTopRatedMovies(apiKey, nextPage) }
        Category.UPCOMING -> { apiService.getUpcomingMovies(apiKey, nextPage) }
    }

    private fun getDownloadedMovie(m: Movie) = movies.add(m)

    private fun updateLiveData() = moviesLiveData.postValue(Triple(movies, currentPage, category))

    private fun updateLiveDataWithError() {
        val anyCategory = Category.POPULAR
        moviesLiveData.postValue(Triple(movies, ERROR_FLAG, anyCategory))
    }

}
