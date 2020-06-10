package com.zywczas.bestonscreen.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.webservice.TMDBService
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.logD
import com.zywczas.bestonscreen.utilities.toMovie
import com.zywczas.bestonscreen.utilities.toMovieFromDB
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.exitProcess

@Singleton
class MovieRepository @Inject constructor(
    private val compositeDispMovies: CompositeDisposable,
    private val compositeDispMovieDetails: CompositeDisposable,
    private val movies: ArrayList<Movie>,
    private val movieLd: MutableLiveData<Movie>,
    private val moviesEventLd: MutableLiveData<Event<List<Movie>>>,
    val moviesLd: MutableLiveData<List<Movie>>,
    private val tmdbService: TMDBService,
    private val movieDao: MovieDao,
    private val booleanLd: MutableLiveData<Boolean>,
    private val booleanEventLd: MutableLiveData<Event<Boolean>>,
    private val intEventLd: MutableLiveData<Event<Int>>,
    val stringEventLd : MutableLiveData<Event<String>>
) {

    fun clearMoviesDisposables() {
        compositeDispMovies.clear()
    }

    fun clearMovieDetailsDisposables() {
        compositeDispMovieDetails.clear()
    }

    private fun log(t: Throwable?) =
        Log.d("film error", "${t?.localizedMessage}")

//    fun getMoviesFromApi (category: Category) : MutableLiveData<Event<List<Movie>>> {
//        movies.clear()
//
//        val moviesObservableApi = when (category) {
//            Category.POPULAR -> { tmdbService.getPopularMovies() }
//            Category.TOP_RATED -> { tmdbService.getTopRatedMovies() }
//            Category.UPCOMING -> { tmdbService.getUpcomingMovies() }
//            else -> { exitProcess(0)}
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
//                    moviesEventLd.postValue(Event(movies))
//                }
//
//                override fun onNext(m: Movie?) {
//                    if (m != null) {
//                        movies.add(m)
//                    }
//                }
//
//                override fun onError(e: Throwable?) {
//                    e?.localizedMessage?.let { logD(it) }
//                }
//            })
//        )
//        return moviesEventLd
//    }

    fun getMoviesFromApi (category: Category) : MutableLiveData<List<Movie>> {
        movies.clear()

        val moviesObservableApi = when (category) {
            Category.POPULAR -> { tmdbService.getPopularMovies() }
            Category.TOP_RATED -> { tmdbService.getTopRatedMovies() }
            Category.UPCOMING -> { tmdbService.getUpcomingMovies() }
            else -> { exitProcess(0)}
        }

        compositeDispMovies.add(moviesObservableApi
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { movieApiResponse -> Observable.fromArray(*movieApiResponse.movies!!.toTypedArray()) }
            .flatMap { movieFromApi ->
                //converts genres 'IDs' to names (e.g. 123 -> "Family movie")
                movieFromApi.genreIds?.let { movieFromApi.convertGenres(it) }
                //converts MovieFromAPI to Observable <Movie>
                Observable.just( toMovie(movieFromApi) )
            }
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onComplete() {
                    logD("wysyla liste z API")
                    moviesLd.postValue(movies)
                }

                override fun onNext(m: Movie?) {
                    if (m != null) {
                        movies.add(m)
                    }
                }

                override fun onError(e: Throwable?) {
                    log(e)
                }
            })
        )
        return moviesLd
    }

//    fun getMoviesFromDB () : MutableLiveData<Event<List<Movie>>> {
//
//        val moviesObservableDB = RxJavaBridge.toV3Flowable(movieDao.getMovies())
//
//        compositeDispMovies.add(
//            moviesObservableDB
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .onBackpressureBuffer()
//                //converts MovieFromDB to list of general Movie class
//                .map { moviesFromDB ->
//                    movies.clear()
////                    Log.d("film test", "map")
//                    for (e in moviesFromDB) {
//                        movies.add(toMovie(e))
//                    }
//                    movies
//                }
//                //Consumer onNext & onError
//                .subscribe({ listOfMovies ->  moviesEventLd.postValue(Event(listOfMovies))
////                    Log.d("film test", "get movies on onNext")
//                }, { it?.localizedMessage?.let { it1 -> logD(it1) } }
//                )
////
//
//        )
//        return moviesEventLd
//    }

    fun getMoviesFromDB () : MutableLiveData<List<Movie>> {

        val moviesObservableDB = RxJavaBridge.toV3Flowable(movieDao.getMovies())

        compositeDispMovies.add(
            moviesObservableDB
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                //converts MovieFromDB to list of general Movie class
                .map { moviesFromDB ->
                    logD("pobiera liste z db")
                    movies.clear()
//                    Log.d("film test", "map")
                    for (e in moviesFromDB) {
                        movies.add(toMovie(e))
                    }
                    movies
                }
                //Consumer onNext & onError
                .subscribe({ movies ->  moviesLd.postValue(movies)
//                    Log.d("film test", "get movies on onNext")
                }, { log(it) }
                )
        )
        return moviesLd
    }

    /**
     * Adds a movie to the local data base so it can be displayed later (different fun)
     * in "To Watch List"
     */
    fun addMovieToDB (movie: Movie) : MutableLiveData<Event<String>> {
        val completableRx3 = RxJavaBridge.toV3Completable(
            movieDao.addMovie(toMovieFromDB(movie))
        )
        compositeDispMovieDetails.add(
            completableRx3
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver(){
                    override fun onComplete() {
                        stringEventLd.postValue(Event("Movie added to your list"))
                    }
                    override fun onError(e: Throwable?) {
                         log(e)
                        stringEventLd.postValue(Event("Problem with adding the movie"))
                    }
                })
        )
        return stringEventLd
    }

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

    fun checkIfMovieIsInDB (movieId: Int) : MutableLiveData<Event<Boolean>> {

        val movieFromDBObservable = RxJavaBridge.toV3Observable(movieDao.checkIfExists(movieId))
        compositeDispMovieDetails.add(
            movieFromDBObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({when(it){
                    1 -> booleanEventLd.postValue(Event(true))
                    0 -> booleanEventLd.postValue(Event(false))
                }}, { log(it) }
                )
        )
        return booleanEventLd
    }

//    fun getMovieFromDB(movieId: Int, context: Context) : MutableLiveData<Movie>{
//        val movieFromDBObservableRx3 = RxJavaBridge.toV3Observable(movieDao.getMovie(movieId))
//        compositeDisposable.add(
//            movieFromDBObservableRx3
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                //converts MovieFromDB to Observable<Movie>
//                .flatMap { movieFromDB -> Observable.just(toMovie(movieFromDB)) }
//                    //Consumer which accepts single Movie and Throwable
//                .subscribe({ movie-> movieMutableLiveData.postValue(movie) },
//                    { t -> Log.d("error", "${t?.localizedMessage}")
//                    showToast("Problem with receiving the movie from DB")
//                    }
//                )
//        )
//        return movieMutableLiveData
//    }

    fun deleteMovieFromDB(movie : Movie) :  MutableLiveData<Event<String>> {
        val completableRx3 = RxJavaBridge.toV3Completable(
            movieDao.deleteMovie(toMovieFromDB(movie))
        )

        compositeDispMovieDetails.add(
            completableRx3
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver(){
                    override fun onComplete() {
                        stringEventLd.postValue(Event("Movie deleted from your list"))
                    }

                    override fun onError(e: Throwable?) {
                        stringEventLd.postValue(Event("Problem with deleting the movie"))
                        log(e)
                    }

                })
        )
        return stringEventLd
    }

}