package com.zywczas.bestonscreen.model


import com.zywczas.bestonscreen.model.webservice.TMDBService
import com.zywczas.bestonscreen.utilities.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.exitProcess

@Singleton
class ApiMoviesRepo @Inject constructor(
    private val compositeDisposables: CompositeDisposable,
    private val movies: ArrayList<Movie>,
    private val tmdbService: TMDBService,
    private val movieListLE : LiveEvent<PairMoviesInt>
) {
    private var currentPage = 1
    //just any number bigger >= 1 at the beginning
    private var lastPage = 1

    fun clearDisposables() = compositeDisposables.clear()

    fun getMoviesFromApi (category: String, page: Int) : LiveEvent<PairMoviesInt> {
        movies.clear()

        if (page > lastPage) {
            //sends 0 as a flag to Observer
            logD("page sie nie zgadza w repo")
            movieListLE.postValue(PairMoviesInt(movies, 0))
            return  movieListLE
        }

        val moviesObservableApi = when (category) {
            POPULAR -> { tmdbService.getPopularMovies(API_KEY, page) }
            TOP_RATED -> { tmdbService.getTopRatedMovies(API_KEY, page) }
            UPCOMING -> { tmdbService.getUpcomingMovies(API_KEY, page) }
            //this option sends empty LiveEvent just to remove observers
            EMPTY_CATEGORY -> {
                movieListLE.postValue(PairMoviesInt(movies, currentPage))
                return  movieListLE }
            else -> { logD("incorrect movie category passed to 'getMoviesFromApi'")
                exitProcess(0)}
        }

        compositeDisposables.add(moviesObservableApi
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .flatMap { movieApiResponse ->
                movieApiResponse.page?.let { currentPage = it
                logD("leci strona: $it")}
                movieApiResponse.totalPages?.let { lastPage = it }
                Observable.fromArray(*movieApiResponse.movies!!.toTypedArray()) }

            .flatMap { movieFromApi ->
                //converts genres 'IDs' to names (e.g. 123 -> "Family movie")
                movieFromApi.genreIds?.let { movieFromApi.convertGenres(it) }
                //converts MovieFromAPI to Observable <Movie>
                Observable.just( toMovie(movieFromApi) )
            }
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onComplete() {
                    logD("wysyla liste z API")
                    movieListLE.postValue(PairMoviesInt(movies, currentPage))
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
        return movieListLE
    }

}
