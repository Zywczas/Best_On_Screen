package com.zywczas.bestonscreen.model.repositories

import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.toMovie
import com.zywczas.bestonscreen.model.webservice.NetworkMovieService
import com.zywczas.bestonscreen.model.webservice.NetworkMovie
import com.zywczas.bestonscreen.utilities.API_KEY
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

open class NetworkMoviesRepository @Inject constructor(private val networkMovieService: NetworkMovieService) {

    private val apiKey by lazy { API_KEY }
    private val invalidApiKeyStatus by lazy { "HTTP 401" }
    private val noMorePagesStatus by lazy { "HTTP 422" }
    private val invalidApiKeyError by lazy { "Invalid API key. Contact technical support." }
    private val noMorePagesError by lazy { "No more pages in this category." }
    private val generalApiError by lazy { "Problem with downloading movies. Check connection and try again." }

    open fun getApiMovies(category: Category, page: Int): Flowable<Resource<List<Movie>>> {
        val apiSingle = getApiSingle(category, page)
        return apiSingle
            .subscribeOn(Schedulers.io())
            .map { apiResponse ->
                val movies = apiResponse.movies?.let { convertToMovies(it) }
                if (movies.isNullOrEmpty()) {
                    Resource.error(noMorePagesError, null)
                } else {
                    Resource.success(movies)
                }
            }
            .onErrorReturn { e -> getError(e) }
            .toFlowable()
    }

    private fun getApiSingle(category: Category, page: Int) = when (category) {
        Category.POPULAR -> {
            networkMovieService.getPopularMovies(apiKey, page)
        }
        Category.TOP_RATED -> {
            networkMovieService.getTopRatedMovies(apiKey, page)
        }
        Category.UPCOMING -> {
            networkMovieService.getUpcomingMovies(apiKey, page)
        }
    }

    private fun convertToMovies(moviesFromApi: List<NetworkMovie>): List<Movie> {
        val movies = mutableListOf<Movie>()
        for (m in moviesFromApi) {
            movies.add(toMovie(m))
        }
        return movies
    }

    private fun getError(e: Throwable): Resource<List<Movie>> {
        return when (e.message.toString().trim()) {
            invalidApiKeyStatus ->
                Resource.error(invalidApiKeyError, null)
            noMorePagesStatus ->
                Resource.error(noMorePagesError, null)
            else -> {
                Resource.error(generalApiError, null)
            }
        }
    }

}
