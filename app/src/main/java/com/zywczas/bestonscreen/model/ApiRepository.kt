package com.zywczas.bestonscreen.model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.model.webservice.MovieFromApi
import com.zywczas.bestonscreen.utilities.ERROR_FLAG
import com.zywczas.bestonscreen.utilities.NO_MORE_PAGES_FLAG
import com.zywczas.bestonscreen.utilities.logD
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(
    private val movies: ArrayList<Movie>,
    private val apiService: ApiService
) {

    private val apiKey = "43a74b6228b35b23e401df1c6a464af1"
    private var nextPage = 1
    private lateinit var category: Category

    fun getApiMovies(category: Category, nextPage: Int): Flowable<Triple<List<Movie>, Int, Int>> {
        movies.clear()
        this.category = category
        this.nextPage = nextPage
        val apiSingle = getApiSingle()
        return apiSingle
            .subscribeOn(Schedulers.io())
            .map { apiResponse ->
                val currentPage = apiResponse.page ?: 0
                val lastPageOfCategory = apiResponse.totalPages ?: 0
                apiResponse.movies?.let { convertIdsAndToMovies(it) }
                Triple(movies.toList(), currentPage, lastPageOfCategory)
            }
            .toFlowable()
    }

    private fun getApiSingle() = when (category) {
        Category.POPULAR -> {
            apiService.getPopularMovies(apiKey, nextPage)
        }
        Category.TOP_RATED -> {
            apiService.getTopRatedMovies(apiKey, nextPage)
        }
        Category.UPCOMING -> {
            apiService.getUpcomingMovies(apiKey, nextPage)
        }
    }

    private fun convertIdsAndToMovies(moviesFromApi: List<MovieFromApi>){
        for (m in moviesFromApi) {
            m.convertGenreIdsToVariables()
            movies.add(toMovie(m))
        }
    }

    //todo sprobowac usunac observe on tam gdzie mamy live data

}
