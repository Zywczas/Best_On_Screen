package com.zywczas.bestonscreen.model


import android.util.Log
import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.model.webservice.MovieFromApi
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(
    private val apiService: ApiService
) {

    private val apiKey = "43a74b6228b35b23e401df1c6a464af1"
    private val invalidApiKeyStatus = "HTTP 401"
    private val noMorePagesStatus = "HTTP 422"
    private val movies = mutableListOf<Movie>()

    fun getApiMovies(category: Category, page: Int): Flowable<Resource<List<Movie>>> {
        movies.clear()
        val apiSingle = getApiSingle(category, page)
        return apiSingle
            .subscribeOn(Schedulers.io())
            .map { apiResponse ->
                apiResponse.movies?.let { convertIdsAndToMovies(it) }
                Resource.success(movies.toList())
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

    private fun convertIdsAndToMovies(moviesFromApi: List<MovieFromApi>) {
        for (m in moviesFromApi) {
            m.convertGenreIdsToVariables()
            movies.add(toMovie(m))
        }
    }

    private fun getError(e: Throwable) : Resource<List<Movie>>{
        return when (e.message.toString().trim()) {
            invalidApiKeyStatus ->
                Resource.error("Invalid API key. Contact technical support.", null)
            noMorePagesStatus ->
                Resource.error("No more pages in this category.", null)
            else ->
                Resource.error("Problem with downloading movies. Check connection.",null)
        }
    }

}
