package com.zywczas.bestonscreen.model.webservice

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NetworkMovie(

    @SerializedName("id")
    @Expose
    val id: Int?,

    @SerializedName("poster_path")
    @Expose
    val posterPath: String?,

    @SerializedName("genre_ids")
    @Expose
    val genres: List<Int>?,

    @SerializedName("title")
    @Expose
    val title: String?,

    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double?,

    @SerializedName("overview")
    @Expose
    val overview: String?,

    @SerializedName("release_date")
    @Expose
    val releaseDate: String?
) {

    fun getGenresDescription(): String =
        if (genres.isNullOrEmpty()) {
            "Genres: no information"
        } else if (genres.size == 1) {
            "Genre: ${genres[0].toGenre()}"
        } else {
            "Genres: " + genres.joinToString(separator = ", ") { it.toGenre() }
        }

    private fun Int.toGenre(): String = when (this) {
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
        10770 -> "TV Movie"
        53 -> "Thriller"
        10752 -> "War"
        37 -> "Western"
        else -> "unknown"
    }
}
