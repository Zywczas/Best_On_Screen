package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.utilities.MOVIE_POPULAR
import com.zywczas.bestonscreen.utilities.MOVIE_TOP_RATED
import com.zywczas.bestonscreen.utilities.MOVIE_UPCOMING
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

//interface for contacting with https://www.themoviedb.org/
interface TMDBService {

    @GET(MOVIE_POPULAR)
    fun getPopularMovies(@Query ("api_key") apiKey: String) : Observable<MovieApiResponse>

    @GET(MOVIE_UPCOMING)
    fun getUpcomingMovies(@Query ("api_key") apiKey: String) : Observable<MovieApiResponse>

    @GET(MOVIE_TOP_RATED)
    fun getTopRatedMovies(@Query("api_key") apiKey: String) : Observable<MovieApiResponse>
}