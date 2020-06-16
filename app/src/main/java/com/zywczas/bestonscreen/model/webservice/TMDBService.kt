package com.zywczas.bestonscreen.model.webservice

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**Interface for contacting with https://www.themoviedb.org/
 *
 */
interface TMDBService {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ) : Observable<MovieApiResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ) : Observable<MovieApiResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ) : Observable<MovieApiResponse>

    //this method is unnecessary for now
//    @GET("movie/{movie_id}?api_key=$API_KEY")
//    fun getMovieDetails(@Path ("movie_id") movieId : Int) : Observable<MovieFromApi>
}