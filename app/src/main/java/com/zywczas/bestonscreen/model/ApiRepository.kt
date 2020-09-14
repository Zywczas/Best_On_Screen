package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.model.webservice.MovieFromApi
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(
    private val apiService: ApiService
) {

    private val apiKey = "43a74b6228b35b23e401df1c6a464af1"
    val invalidApiKeyStatus = "HTTP 401"
    val noMorePagesStatus = "HTTP 422"
    //todo albo wrzucic wszystkie rrror w val albo cofnac ten do funkcji
    val invalidApiKeyError = "Invalid API key. Contact technical support."
    val noMorePagesError = "No more pages in this category."
    val generalApiError = "Problem with downloading movies. Close app and try again."
    val noMoviesError = "Couldn't download more movies. Try again."

    fun getApiMovies(category: Category, page: Int): Flowable<Resource<List<Movie>>> {
        val apiSingle = getApiSingle(category, page)
        return apiSingle
            .subscribeOn(Schedulers.io())
            .map { apiResponse ->
                val movies = apiResponse.movies?.let { convertToMovies(it) }
                if (movies != null) {
                    Resource.success(movies)
                } else {
                    Resource.error(noMoviesError, null)
                }
            }
            .onErrorReturn { e -> getError(e) }
            .toFlowable()
    }

    private fun getApiSingle(category: Category, page: Int) = when (category) {
        Category.POPULAR -> {
            apiService.getPopularMovies(apiKey, page)
        }
        Category.TOP_RATED -> {
            apiService.getTopRatedMovies(apiKey, page)
        }
        Category.UPCOMING -> {
            apiService.getUpcomingMovies(apiKey, page)
        }
    }

    private fun convertToMovies(moviesFromApi: List<MovieFromApi>) : List<Movie> {
        val movies = mutableListOf<Movie>()
        for (m in moviesFromApi) {
            movies.add(toMovie(m))
        }
        return movies
    }

    private fun getError(e: Throwable) : Resource<List<Movie>>{
        return when (e.message.toString().trim()) {
            invalidApiKeyStatus ->
                Resource.error(invalidApiKeyError, null)
            noMorePagesStatus ->
                Resource.error(noMorePagesError, null)
            else -> {
                Resource.error(generalApiError,null)   }
        }
    }

}
