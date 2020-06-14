package com.zywczas.bestonscreen.model


import android.graphics.pdf.PdfDocument
import androidx.lifecycle.MutableLiveData
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
    private val movieListLE : LiveEvent<Pair<List<Movie>, Int>>
) {
    private var currentPage = 1
    //just any number bigger than 1 at the beginning
    private var lastPage = 9

    fun clearDisposables() = compositeDisposables.clear()

    fun getMoviesFromApi (category: String, page: Int) : LiveEvent<Pair<List<Movie>, Int>> {
        //if new category coming then reset the list
        if (page == 1){
            movies.clear()
        }
        if (page > lastPage) {
            //sends 0 as a flag to Observer
            movieListLE.postValue(Pair(movies, 0))
            return  movieListLE
        }

        val moviesObservableApi = when (category) {
            POPULAR -> { tmdbService.getPopularMovies(API_KEY, page) }
            TOP_RATED -> { tmdbService.getTopRatedMovies(API_KEY, page) }
            UPCOMING -> { tmdbService.getUpcomingMovies(API_KEY, page) }
            //this option sends empty LiveEvent just to remove observers
            REMOVE_OBSERVER -> { movies.clear()
                movieListLE.postValue(Pair(movies, currentPage))
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

                movieApiResponse.totalPages?.let { lastPage = it

                    logD("total pages: $it") }



                Observable.fromArray(*movieApiResponse.movies!!.toTypedArray()) }
            .flatMap { movieFromApi ->
                //converts genres 'IDs' to names (e.g. 123 -> "Family movie")
                movieFromApi.genreIds?.let { movieFromApi.convertGenres(it) }
                //converts MovieFromAPI to Observable <Movie>
                Observable.just( toMovie(movieFromApi) )
            }
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onComplete() {
                    movieListLE.postValue(Pair(movies, currentPage))
                    logD("wysyla liste z API, strona $currentPage")
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

//    fun getMoviesFromApi (category: String) : MutableLiveData<Event<List<Movie>>> {
//        movies.clear()
//
//        val moviesObservableApi = when (category) {
//            POPULAR -> { tmdbService.getPopularMovies() }
//            TOP_RATED -> { tmdbService.getTopRatedMovies() }
//            UPCOMING -> { tmdbService.getUpcomingMovies() }
//            else -> {
//                logD("incorrect movie category passed to 'getMoviesFromApi'")
//                exitProcess(0)}
//        }
//
//        compositeDisposables.add(moviesObservableApi
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .flatMap { movieApiResponse -> Observable.fromArray(*movieApiResponse.movies!!.toTypedArray()) }
//            .flatMap { movieFromApi ->
//                //converts genres 'IDs' to names (e.g. 123 -> "Family movie")
//                movieFromApi.genreIds?.let { movieFromApi.convertGenres(it) }
//                //converts MovieFromAPI to Observable <Movie>
//                Observable.just( toMovie(movieFromApi) )
//            }
//            .subscribeWith(object : DisposableObserver<Movie>() {
//                override fun onComplete() {
//                    moviesEventLd.postValue(Event(movies))
//                    logD("wysyla liste z API")
//                }
//
//                override fun onNext(m: Movie?) {
//                    if (m != null) {
//                        movies.add(m)
//                    }
//                }
//
//                override fun onError(e: Throwable?) {
//                    logD(e)
//                }
//            })
//        )
//        return moviesEventLd
//    }

//    fun getMoviesFromApi (category: String) : MutableLiveData<List<Movie>> {
//        movies.clear()
//
//        val moviesObservableApi = when (category) {
//            POPULAR -> { tmdbService.getPopularMovies() }
//            TOP_RATED -> { tmdbService.getTopRatedMovies() }
//            UPCOMING -> { tmdbService.getUpcomingMovies() }
//            else -> {
//                logD("incorrect movie category passed to 'getMoviesFromApi'")
//                exitProcess(0)}
//        }
//
//        compositeDispMovies.add(moviesObservableApi
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .flatMap { movieApiResponse -> Observable.fromArray(*movieApiResponse.movies!!.toTypedArray()) }
//            .flatMap { movieFromApi ->
//                //converts genres 'IDs' to names (e.g. 123 -> "Family movie")
//                movieFromApi.genreIds?.let { movieFromApi.convertGenres(it) }
//                //converts MovieFromAPI to Observable <Movie>
//                Observable.just( toMovie(movieFromApi) )
//            }
//            .subscribeWith(object : DisposableObserver<Movie>() {
//                override fun onComplete() {
//                    logD("wysyla liste z API")
//                    moviesLd.postValue(movies)
//                }
//
//                override fun onNext(m: Movie?) {
//                    if (m != null) {
//                        movies.add(m)
//                    }
//                }
//
//                override fun onError(e: Throwable?) {
//                    logD(e)
//                }
//            })
//        )
//        return moviesLd
//    }



//    fun getMoviesFromDB () : MutableLiveData<List<Movie>> {
//
//        val moviesObservableDB = RxJavaBridge.toV3Flowable(movieDao.getMovies())
//
//    compositeDispDBMovies.add(
//            moviesObservableDB
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .onBackpressureBuffer()
//                //converts MovieFromDB to list of general Movie class
//                .map { moviesFromDB ->
//                    logD("pobiera liste z db")
//                    movies.clear()
////                    Log.d("film test", "map")
//                    for (e in moviesFromDB) {
//                        movies.add(toMovie(e))
//                    }
//                    movies
//                }
//                //Consumer onNext & onError
//                .subscribe({ movies ->  moviesLd.postValue(movies)
////                    Log.d("film test", "get movies on onNext")
//                }, { logD(it) }
//                )
//        )
//        return moviesLd
//    }


//    fun checkIfMovieIsInDB (movieId: Int) : MutableLiveData<Boolean> {
//
//        val movieFromDBObservable = RxJavaBridge.toV3Observable(movieDao.checkIfExists(movieId))
//        compositeDispMovieDetails.add(
//            movieFromDBObservable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({when(it){
//                    1 -> booleanLiveData.postValue(true)
//                    0 -> booleanLiveData.postValue(false)
//                }}, { logD(it)}
//                )
//        )
//        return booleanLiveData
//    }



//    fun getMovieFromDB(movieId: Int, context: Context) : MutableLiveData<Movie>{
//        val movieFromDBObservableRx3 = RxJavaBridge.toV3Observable(movieDao.getMovie(movieId))
//        compositeDisposable.add(
//            movieFromDBObservableRx3
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                //converts MovieFromDB to Observable<Movie>
//                .flatMap { movieFromDB -> Observable.just(toMovie(movieFromDB)) }
//                    //Consumer which accepts single Movie and Throwable
//                .subscribe({ movie-> movieMutableLiveData.postValue(movie) }
//                    { t -> Log.d("error", "${t?.localizedMessage}")
//                    showToast("Problem with receiving the movie from DB")
//                    }
//                )
//        )
//        return movieMutableLiveData
//    }


}
