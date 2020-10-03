package com.zywczas.bestonscreen.model

import android.util.Log
import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.model.webservice.MovieFromApi
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.utilities.Status
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

open class ApiRepository @Inject constructor(private val apiService: ApiService) {

    private val apiKey by lazy { "43a74b6228b35b23e401df1c6a464af1" }
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
            apiService.getPopularMovies(apiKey, page)
        }
        Category.TOP_RATED -> {
            apiService.getTopRatedMovies(apiKey, page)
        }
        Category.UPCOMING -> {
            apiService.getUpcomingMovies(apiKey, page)
        }
    }

    private fun convertToMovies(moviesFromApi: List<MovieFromApi>): List<Movie> {
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
