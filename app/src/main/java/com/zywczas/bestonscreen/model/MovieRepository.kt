package com.zywczas.bestonscreen.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.webservice.MovieApiResponse
import com.zywczas.bestonscreen.model.webservice.TMDBService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val compositeDisposable: CompositeDisposable,
    private val movies: ArrayList<Movie>,
    private val moviesLiveData: MutableLiveData<List<Movie>>,
    private val tmdbService: TMDBService
) {

    private lateinit var moviesObservable: Observable<MovieApiResponse>
//    private lateinit var movieDetailsObservable: Observable<MovieFromApi>

    fun clear() = compositeDisposable.clear()

    fun downloadMovies (context: Context, category: Category) : MutableLiveData<List<Movie>> {
        movies.clear()

        moviesObservable = when (category) {
            Category.POPULAR -> {
                tmdbService.getPopularMovies()
            }
            Category.TOP_RATED -> {
                tmdbService.getTopRatedMovies()
            }
            Category.UPCOMING -> {
                tmdbService.getUpcomingMovies()
            }
        }

        compositeDisposable.add(moviesObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { movieApiResponse -> Observable.fromArray(*movieApiResponse.movies!!.toTypedArray()) }
            .flatMap { movieFromApi ->
                    movieFromApi.genreIds?.let { movieFromApi.convertGenres(it) }                         //converts genres 'IDs' to names (e.g. 123 -> "Family movieFromApi")

                    Observable.just(movieFromApi.toMovie(movieFromApi))
            }
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onComplete() {
                    moviesLiveData.postValue(movies)
                    Toast.makeText(context, category.toString(), Toast.LENGTH_LONG).show()
                }

                override fun onNext(m: Movie?) {
                    if (m != null) {
                        movies.add(m)
                    }
                }

                override fun onError(e: Throwable?) {
                    Toast.makeText(
                        context,
                        "Problem with downloading movies",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("ERROR", "${e?.localizedMessage}")
                }
            })
        )
        return moviesLiveData
    }

    //this method is unnecessary for now
//    fun getMovieDetailsLiveData (context: Context, movieId: Int) : MutableLiveData<MovieFromApi> {
//        movieDetailsObservable = tmdbService.getMovieDetails(movieId)
//
//        compositeDisposable.add(
//            movieDetailsObservable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({movie -> movieDetailsLiveData.postValue(movie) },
//                    {error -> Toast.makeText(context, "Problem with downloading movie details", Toast.LENGTH_LONG).show()
//                        Log.d("ERROR", "${error?.localizedMessage}")
//                    })
//        )
//        return movieDetailsLiveData
//    }

}