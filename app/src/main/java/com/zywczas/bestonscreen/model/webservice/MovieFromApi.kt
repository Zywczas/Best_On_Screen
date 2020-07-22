package com.zywczas.bestonscreen.model.webservice

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieFromApi {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null

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

    var assignedGenresAmount: Int = 0

    fun assignGenresListToVariables(genreIds: List<Int>){
        var nextGenreVar = 1

        for (id in genreIds) {
            assignToVariable(id, nextGenreVar)
            nextGenreVar++
        }
    }

    private fun assignToVariable(id: Int, nextGenreVar: Int) {
        val genreTemp = convertToString(id)
        val availableGenreVars = 5
//todo to dokonczyc
        if (genreTemp != null && nextGenreVar <= availableGenreVars) {
            when (nextGenreVar) {
                1 -> {genre1 = genreTemp
                    assignedGenresAmount = 1}
                2 -> {genre2 = genreTemp
                    assignedGenresAmount = 2}
                3 -> {genre3 = genreTemp
                    assignedGenresAmount = 3}
                4 -> {genre4 = genreTemp
                    assignedGenresAmount = 4}
                5 -> {genre5 = genreTemp
                    assignedGenresAmount = 5 }
            }
        }
    }

    private fun convertToString(id: Int) : String? {
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
            else -> null
        }
    }

}