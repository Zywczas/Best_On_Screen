package com.zywczas.bestonscreen.model.webservice

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieFromApi(

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
    lateinit var genresDescription : String

    fun convertGenreIdsToDescription() {
        //todo test na empty
        genresDescription = if (genres != null) {
            when (genres.size) {
                1 -> "Genre: ${toStr(genres[0])}"
                2 -> "Genres: ${toStr(genres[0])}, ${toStr(genres[1])}"
                3 -> "Genres: ${toStr(genres[0])}, ${toStr(genres[1])}, ${toStr(genres[2])}"
                4 -> "Genres: ${toStr(genres[0])}, ${toStr(genres[1])}, ${toStr(genres[2])}, " +
                        toStr(genres[3])
                5 -> "Genres: ${toStr(genres[0])}, ${toStr(genres[1])}, ${toStr(genres[2])}, " +
                        "${toStr(genres[3])}, ${toStr(genres[4])}"
                else -> "Genres: missing information"
            }
        } else {
            "Genres: no information"
        }
    }

    private fun toStr(id: Int): String {
        return when (id) {
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
            else -> "missing info"
        }
    }

}
