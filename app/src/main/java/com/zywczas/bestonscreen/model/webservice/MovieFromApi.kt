package com.zywczas.bestonscreen.model.webservice

import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Model class for Api response
 */

class MovieFromApi {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("popularity")
    @Expose
    var popularity: Double? = null

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int? = null

    @SerializedName("video")
    @Expose
    var video: Boolean? = null

    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null

    @SerializedName("adult")
    @Expose
    var adult: Boolean? = null

    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null

    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null

    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null

    @SerializedName("genre_ids")
    @Expose
    var genreIds: List<Int>? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null

    @SerializedName("overview")
    @Expose
    var overview: String? = null

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null

    var genre1: String? = null

    var genre2: String? = null

    var genre3: String? = null

    var genre4: String? = null

    var genre5: String? = null

    var genresAmount: Int = 0

    /**
    This converter is required as API gives List<Int> of genres instead of names (String).
    I could download names of genres using TMDBService fun getMovieDetails() but it is unnecessary
    for now as I don't need any more data from getMovieDetails(). This function is used in
    ApiMoviesRepo class.
     *@param genreIds - a list of Int from API, which needs to be converted to Strings and assigned
     * to variables genre1 - genre5
     */
    fun convertGenres(genreIds: List<Int>){
        lateinit var genreTemp: String
        loop@ for (id in genreIds) {
            genreTemp = when (id) {
                28 -> "Action"
                12 -> "Adventure"
                16 -> "Animation"
                35 -> "Comedy"
                80 -> "Crime"
                99 -> "Documentary"
                18 -> "Drama"
                10751 -> "Family"
                14 -> "Fantasy"
                36 -> "History"
                27 -> "Horror"
                10402 -> "Music"
                9648 -> "Mystery"
                10749 -> "Romance"
                878 -> "Science Fiction"
                10770 -> "TV MovieFromApi"
                53 -> "Thriller"
                10752 -> "War"
                37 -> "Western"
                else -> { Log.d("error", "Incorrect genre id")
                    continue@loop
                }
            }
            when (genresAmount) {
                0 -> {genre1 = genreTemp
                    genresAmount = 1}
                1 -> {genre2 = genreTemp
                    genresAmount = 2}
                2 -> {genre3 = genreTemp
                    genresAmount = 3}
                3 -> {genre4 = genreTemp
                    genresAmount = 4}
                4 -> {genre5 = genreTemp
                    genresAmount = 5
                    break@loop}
                else -> { Log.d("error", "problem with genre converter")
                    break@loop}
            }
        }

    }

}