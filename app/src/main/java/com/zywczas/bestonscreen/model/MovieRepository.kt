package com.zywczas.bestonscreen.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.localstore.MovieDao
import com.zywczas.bestonscreen.model.webservice.TMDBService
import com.zywczas.bestonscreen.utilities.Event
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
    private val movieMutableLiveData: MutableLiveData<Movie>,
    private val moviesMutableLdApi: MutableLiveData<Event<List<Movie>>>,
    private val moviesMutableLdDB: MutableLiveData<List<Movie>>,
    private val tmdbService: TMDBService,
    private val movieDao: MovieDao,
    private val booleanLiveData: MutableLiveData<Boolean>,
    private val booleanEventLd: MutableLiveData<Event<Boolean>>,
    private val intEventLd: MutableLiveData<Event<Int>>,
    private val stringEventLd : MutableLiveData<Event<String>>
) {

    fun clearMoviesActivity() {
        compositeDispMovies.clear()
    }

    fun clearMovieDetailsActivity() {
        compositeDispMovieDetails.clear()
    }

    fun getMoviesFromApi (category: Category) : MutableLiveData<Event<List<Movie>>> {
        movies.clear()

        val moviesObservableApi = when (category) {
            Category.POPULAR -> { tmdbService.getPopularMovies() }
            Category.TOP_RATED -> { tmdbService.getTopRatedMovies() }
            Category.UPCOMING -> { tmdbService.getUpcomingMovies() }
            else -> { Log.d("film error", "getMoviesFromApi exit process: incorrect movie category")
                exitProcess(0)}
        }

        compositeDispMovies.add(moviesObservableApi
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { movieApiResponse -> Observable.fromArray(*movieApiResponse.movies!!.toTypedArray()) }
            .flatMap { movieFromApi ->
                //converts genres 'IDs' to names (e.g. 123 -> "Family movie")
                movieFromApi.genreIds?.let { movieFromApi.convertGenres(it) }
                //converts MovieFromAPI to Observable <Movie>
                Observable.just(
                    toMovie(
                        movieFromApi
                    )
                )
            }
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onComplete() {
                    moviesMutableLdApi.postValue(Event(movies))
                }

                override fun onNext(m: Movie?) {
                    if (m != null) {
                        movies.add(m)
                    }
                }

                override fun onError(e: Throwable?) {
                    Log.d("film error", "${e?.localizedMessage}")
                }
            })
        )
        return moviesMutableLdApi
    }

    fun getMoviesFromDB () : MutableLiveData<Event<List<Movie>>> {

        val moviesObservableDB = RxJavaBridge.toV3Flowable(movieDao.getMovies())

        compositeDispMovies.add(
            moviesObservableDB
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                //converts MovieFromDB to list of general Movie class
                .map { moviesFromDB ->
                    movies.clear()
//                    Log.d("film test", "map")
                    for (e in moviesFromDB) {
                        movies.add(toMovie(e))
                    }
                    movies
                }
                //Consumer onNext & onError
                .subscribe({ listOfMovies ->  moviesMutableLdApi.postValue(Event(listOfMovies))
//                    Log.d("film test", "get movies on onNext")
                }, {throwable -> Log.d("film error", throwable?.localizedMessage)
                })
//

        )
        return moviesMutableLdApi
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
                        Log.d("film error", "${e?.localizedMessage}")
                        stringEventLd.postValue(Event("Problem with adding the movie"))
                    }
                })
        )
        return stringEventLd
    }

    fun checkIfMovieIsInDB (movieId: Int) : MutableLiveData<Event<Int>> {

        val movieFromDBObservable = RxJavaBridge.toV3Observable(movieDao.checkIfExists(movieId))
        compositeDispMovieDetails.add(
            movieFromDBObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({rowsAmount -> intEventLd.postValue(
                    Event(
                        rowsAmount
                    )
                )},
                    { throwable -> Log.d("film error", "${throwable?.localizedMessage}") }
                )
        )
        return intEventLd
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
//                    Toast.makeText(context, "Problem with receiving the movie from DB", Toast.LENGTH_LONG).show()
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
                        Log.d("film error", "${e?.localizedMessage}")
                    }

                })
        )
        return stringEventLd
    }



}