package com.zywczas.bestonscreen.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.localstore.MovieDao
import com.zywczas.bestonscreen.model.localstore.MovieFromDB
import com.zywczas.bestonscreen.model.webservice.MovieApiResponse
import com.zywczas.bestonscreen.model.webservice.MovieFromApi
import com.zywczas.bestonscreen.model.webservice.TMDBService
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import org.reactivestreams.Subscription
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.exitProcess

@Singleton
class MovieRepository @Inject constructor(
    private val compositeDisposable: CompositeDisposable,
    private val movies: ArrayList<Movie>,
    private val moviesMutableLiveData: MutableLiveData<List<Movie>>,
    private val tmdbService: TMDBService,
    private val movieDao: MovieDao,
    private val booleanLiveData: MutableLiveData<Boolean>
) {

    fun clear() = compositeDisposable.clear()

    fun getMoviesFromApi (context: Context, category: Category) : MutableLiveData<List<Movie>> {
        movies.clear()

        val moviesObservableApi = when (category) {
            Category.POPULAR -> { tmdbService.getPopularMovies() }
            Category.TOP_RATED -> { tmdbService.getTopRatedMovies() }
            Category.UPCOMING -> { tmdbService.getUpcomingMovies() }
            else -> { exitProcess(0)}
        }

        compositeDisposable.add(moviesObservableApi
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { movieApiResponse -> Observable.fromArray(*movieApiResponse.movies!!.toTypedArray()) }
            .flatMap { movieFromApi ->
                //converts genres 'IDs' to names (e.g. 123 -> "Family movie")
                movieFromApi.genreIds?.let { movieFromApi.convertGenres(it) }
                //converts MovieFromAPI to Observable <Movie>
                Observable.just(toMovie(movieFromApi))
            }
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onComplete() {
                    moviesMutableLiveData.postValue(movies)
                    Toast.makeText(context, category.toString(), Toast.LENGTH_LONG).show()
                }

                override fun onNext(m: Movie?) {
                    if (m != null) {
                        movies.add(m)
                    }
                }

                override fun onError(e: Throwable?) {
                    Toast.makeText(context,"Problem with downloading movies",Toast.LENGTH_LONG).show()
                    Log.d("film error", "${e?.localizedMessage}")
                }
            })
        )
        return moviesMutableLiveData
    }

    fun getMoviesFromDB (context: Context, category: Category) : MutableLiveData<List<Movie>> {
        movies.clear()
        val moviesObservableDB = RxJavaBridge.toV3Flowable(movieDao.getMovies())

        compositeDisposable.add(
            moviesObservableDB
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { moviesListFromDB -> Flowable.fromArray(*moviesListFromDB.toTypedArray()) }
                .onBackpressureBuffer()
                //converts MovieFromDB to general Movie class
                .map { movieFromDB -> toMovie(movieFromDB) }
                .subscribeWith(object : DisposableSubscriber<Movie>(){
                    override fun onComplete() {
                        moviesMutableLiveData.postValue(movies)
                        Toast.makeText(context, category.toString(), Toast.LENGTH_LONG).show()
                    }

                    override fun onNext(t: Movie?) {
                        if (t != null) {
                            movies.add(t)
                        }
                    }

                    override fun onError(t: Throwable?) {
                        Log.d("film error", "${t?.localizedMessage}")
                    }
                }
                )
        )
        return moviesMutableLiveData
    }

    /**
     * Adds a movie to the local data base so it can be displayed later (different fun) in "To Watch List"
     * @param movie Movie to be added
     * @param context Context of Activity to show message
     */
    fun addMovieToDB (movie: Movie, context: Context) {
        compositeDisposable.add(
            Completable.fromAction { movieDao.addMovie(toMovieFromDB(movie)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver(){
                    override fun onComplete() {
                        Toast.makeText(context, "Movie added to your list", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("film error", "${e?.localizedMessage}")
                        Toast.makeText(context, "Problem with adding the movie", Toast.LENGTH_SHORT).show()
                    }
                })
        )
    }

    fun checkIfMovieInDB (movieId: Int, context: Context) : MutableLiveData<Boolean> {
        //default value is false, but if movie is found then true
        booleanLiveData.postValue(false)
        val movieFromDBObservable = RxJavaBridge.toV3Observable(movieDao.getMovie(movieId))
        compositeDisposable.add(
            movieFromDBObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                    //Consumer which accepts single Movie and Throwable
                .subscribe({ movieFromDB-> booleanLiveData.postValue(true)
                    Log.d("film error", "film w bazie: ${movieFromDB.title}")
                },
                    { noMovieInDB ->
                        Log.d("film error", "nie ma filmu w bazie") //to poprawic, usunac komentarze jak dziala boolean
                        Toast.makeText(context, "nie ma w bazie", Toast.LENGTH_LONG).show()
                        booleanLiveData.postValue(false)
                    }
                )
        )
        return booleanLiveData
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



}