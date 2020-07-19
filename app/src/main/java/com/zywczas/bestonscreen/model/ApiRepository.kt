package com.zywczas.bestonscreen.model


import androidx.lifecycle.LiveData
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
import kotlin.properties.Delegates

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
    lateinit var category: Category
    var nextPage by Delegates.notNull<Int>()

    fun clearDisposables() = compositeDisposables.clear()

    fun getLiveData() = moviesLiveData as LiveData<Triple<List<Movie>, Int, Category>>

    fun getMoviesFromApi (nextCategory: Category, nextPage: Int) : MutableLiveData<Triple<List<Movie>, Int, Category>> {
        this.category = nextCategory
        this.nextPage = nextPage
        return if (nextPage > lastPageOfCategory) {
            sendFlaggedLiveData()
        } else {
            downloadAndSendMoviesLiveData()
        }
    }

    private fun sendFlaggedLiveData() : MutableLiveData<Triple<List<Movie>, Int, Category>> {
        val anyCategory = Category.POPULAR
        moviesLiveData.postValue(Triple(movies, NO_MORE_PAGES, anyCategory))
        return moviesLiveData
    }

    private fun downloadAndSendMoviesLiveData () : MutableLiveData<Triple<List<Movie>, Int, Category>> {
        if (nextPage == firstPageOfNewCategory ) {
            resetAllMovies()
        }

        val apiResponseObservable = when (category) {
            //todo tu sa powtorzenia z API_Key i page oraz switch powinien byc zastapiony polimorfizmem
            Category.POPULAR -> { apiService.getPopularMovies(apiKey, nextPage) }
            Category.TOP_RATED -> { apiService.getTopRatedMovies(apiKey, nextPage) }
            Category.UPCOMING -> { apiService.getUpcomingMovies(apiKey, nextPage) }
        }

        compositeDisposables.add(apiResponseObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .flatMap { movieApiResponse ->
                movieApiResponse.page?.let { currentPage = it }
                movieApiResponse.totalPages?.let { lastPageOfCategory = it }
                Observable.fromArray(*movieApiResponse.movies!!.toTypedArray()) }

            .flatMap { movieFromApi ->
                movieFromApi.genreIds?.let { movieFromApi.transferGenresListToVariables(it) }
                Observable.just(
                    toMovie(
                        movieFromApi
                    )
                )
            }
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onComplete() {
                    moviesLiveData.postValue(Triple(movies, currentPage,
                        category
                    ))
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
        return moviesLiveData
    }

    private fun resetAllMovies() = movies.clear()
}
