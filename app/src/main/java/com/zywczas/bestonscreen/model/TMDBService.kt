package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.MovieApiResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

//interface for contacting with https://www.themoviedb.org/
interface TMDBService {

    @GET("movie/popular")
    fun getPopularMovies(@Query ("api_key") apiKey: String) : Observable<MovieApiResponse>
}