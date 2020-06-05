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
    private val movieMutableLiveData: MutableLiveData<Movie>,
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
////testowe zmienne
//    var moviesFromDBLD = MutableLiveData<List<MovieFromDB>>()
//    val moviesFromDB = ArrayList<MovieFromDB>()
////to jak na razie to dziala tylko przyjmujac cala liste jako consumer, najwyzej trzeba bedzie przerobic wszystko na movies from DB
//    fun getMoviesFromDB (context: Context, category: Category) : MutableLiveData<List<MovieFromDB>> {
//        movies.clear()
//        val moviesObservableDB = RxJavaBridge.toV3Observable(movieDao.getMovies())
//
//        compositeDisposable.add(
//            moviesObservableDB
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap { moviesListFromDB ->
//                    Log.d("film test", "flatmap z list na movieFromDb")
//                    Observable.fromArray(*moviesListFromDB.toTypedArray())  }
//                .subscribeWith(object : DisposableObserver<MovieFromDB>(){
//                    override fun onComplete() {
//                        Log.d("film test", "on complete")
//                        moviesFromDBLD.postValue(moviesFromDB)
//
//                    }
//
//                    override fun onNext(t: MovieFromDB?) {
//                        if (t != null) {
//                            Log.d("film test", "on next przed")
//                            moviesFromDB.add(t)
//                            Log.d("film test", "on next po")
//                        }
//                    }
//
//                    override fun onError(e: Throwable?) {
//
//                    }
//
//                })
//        )
//        return moviesFromDBLD
//    }

    fun getMoviesFromDB (context: Context, category: Category) : MutableLiveData<Movie> {
        movies.clear()
        val moviesObservableDB = RxJavaBridge.toV3Flowable(movieDao.getMovies())

        compositeDisposable.add(
            moviesObservableDB
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { moviesListFromDB ->
                    Log.d("film test", "flatmap z list na movieFromDb")
                    Flowable.fromArray(*moviesListFromDB.toTypedArray()) }
                .onBackpressureBuffer()
                //converts MovieFromDB to general Movie class
                .map { movieFromDB ->
                    Log.d("film test", "map z movie from db na movie")
                    toMovie(movieFromDB) }
                .subscribe({movie -> movieMutableLiveData.postValue(movie)},
                    {t: Throwable? -> Log.d("film error", t?.localizedMessage) })
//                .subscribe(object : Consumer<List<Movie>>{
//                    override fun accept(t: List<Movie>?) {
//                        moviesMutableLiveData.postValue(t)
//                    }
//
//                }, object : Consumer<Throwable>{
//                    override fun accept(t: Throwable?) {
//                        if (t != null) {
//                            Log.d("film error", t?.localizedMessage)
//                        }
//                    }
//                })
//                .subscribe({listOfMovies -> moviesMutableLiveData.postValue(listOfMovies)},
//                    {t: Throwable? -> Log.d("film error", t?.localizedMessage) })
        )
        return movieMutableLiveData
    }

//    override fun onComplete() {
//        Log.d("film test", "on complete")
//        moviesMutableLiveData.postValue(movies)
//        Toast.makeText(context, category.toString(), Toast.LENGTH_LONG).show()
//    }
//
//    override fun onNext(t: Movie?) {
//        if (t != null) {
//            Log.d("film test", "on next przed")
//            movies.add(t)
//            Log.d("film test", "on next po")
//        }
//    }
//
//    override fun onError(t: Throwable?) {
//        Log.d("film test", "on error")
//        Log.d("film error", "${t?.localizedMessage}")
//    }



    /**
     * Adds a movie to the local data base so it can be displayed later (different fun) in "To Watch List"
     * @param movie Movie to be added
     * @param context Context of Activity to show completion message
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
//sprawdzic czy jak dam Single LiveData albo Event wrapper
    fun checkIfMovieInDB (movieId: Int) : MutableLiveData<Boolean> {

        val movieFromDBObservable = RxJavaBridge.toV3Single(movieDao.getMovie(movieId))
        compositeDisposable.add(
            movieFromDBObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                    //Consumer which accepts single Movie and Throwable
                .subscribe({success -> booleanLiveData.postValue(true)},
                    {error -> booleanLiveData.postValue(false)})
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