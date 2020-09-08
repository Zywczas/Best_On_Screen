package com.zywczas.bestonscreen.model.webservice

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for contacting with https://www.themoviedb.org/
 */
interface ApiService {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ) : Single<ApiResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ) : Single<ApiResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ) : Single<ApiResponse>

}