package com.zywczas.bestonscreen.model.repositories

import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.MovieCategory
import com.zywczas.bestonscreen.model.toMovie
import com.zywczas.bestonscreen.model.webservice.NetworkMovieService
import com.zywczas.bestonscreen.utilities.API_KEY
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class NetworkMoviesRepository @Inject constructor(private val networkMovieService: NetworkMovieService) {

    private val apiKey = API_KEY
    private val invalidApiKeyStatus by lazy { "HTTP 401" }
    private val noMorePagesStatus by lazy { "HTTP 422" }
    private val invalidApiKeyError by lazy { "Invalid API key. Contact technical support." }
    private val noMorePagesError by lazy { "No more pages in this category." }
    private val generalApiError by lazy { "Problem with downloading movies. Check connection and try again." }

    fun getApiMovies(category: MovieCategory, page: Int): Flowable<Resource<List<Movie>>> = getApiSingle(category, page)
        .subscribeOn(Schedulers.io())
        .map { apiResponse ->
            val movies = apiResponse.movies?.map { toMovie(it) }
            if (movies.isNullOrEmpty()) {
                Resource.error(noMorePagesError, null)
            } else {
                Resource.success(movies)
            }
        }
        .onErrorReturn { e -> getError(e) }
        .toFlowable()

    private fun getApiSingle(category: MovieCategory, page: Int) = when (category) {
        MovieCategory.POPULAR -> networkMovieService.getPopularMovies(apiKey, page)
        MovieCategory.TOP_RATED -> networkMovieService.getTopRatedMovies(apiKey, page)
        MovieCategory.UPCOMING -> networkMovieService.getUpcomingMovies(apiKey, page)
    }

    private fun getError(e: Throwable): Resource<List<Movie>> = when (e.message.toString().trim()) {
        invalidApiKeyStatus -> Resource.error(invalidApiKeyError, null)
        noMorePagesStatus -> Resource.error(noMorePagesError, null)
        else -> Resource.error(generalApiError, null)
    }
}
