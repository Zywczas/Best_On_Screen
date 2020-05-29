package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.utilities.API_KEY
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//interface for contacting with https://www.themoviedb.org/
interface TMDBService {

    @GET("movie/popular?api_key=$API_KEY")
    fun getPopularMovies() : Observable<MovieApiResponse>

    @GET("movie/upcoming?api_key=$API_KEY")
    fun getUpcomingMovies() : Observable<MovieApiResponse>

    @GET("movie/top_rated?api_key=$API_KEY")
    fun getTopRatedMovies() : Observable<MovieApiResponse>

    //this method is unnecessary for now
//    @GET("movie/{movie_id}?api_key=$API_KEY")
//    fun getMovieDetails(@Path ("movie_id") movieId : Int) : Observable<Movie>
}