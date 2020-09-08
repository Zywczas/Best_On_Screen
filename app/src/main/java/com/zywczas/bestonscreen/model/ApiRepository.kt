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
    //todo chyba wszystkie mowies mozna wrzucic w klase, sprawdzic przy testach
    private val movies: ArrayList<Movie>,
    private val apiService: ApiService
) {

    private val apiKey = "43a74b6228b35b23e401df1c6a464af1"

    fun getApiMovies(category: Category, page: Int): Flowable<Resource<Pair<List<Movie>, Int>>> {
        movies.clear()
        val apiSingle = getApiSingle(category, page)
        return apiSingle
            .subscribeOn(Schedulers.io())
            .map { apiResponse ->
                val lastPageOfCategory = apiResponse.totalPages ?: 0
                apiResponse.movies?.let { convertIdsAndToMovies(it) }
                Resource.success(Pair(movies.toList(), lastPageOfCategory))
            }
            .onErrorReturn { Resource.error("Problem with downloading movies.", null) }
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
//todo dac tutaj return List
    private fun convertIdsAndToMovies(moviesFromApi: List<MovieFromApi>){
        for (m in moviesFromApi) {
            m.convertGenreIdsToVariables()
            movies.add(toMovie(m))
        }
    }

}
