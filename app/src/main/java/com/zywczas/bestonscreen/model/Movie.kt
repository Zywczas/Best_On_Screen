package com.zywczas.bestonscreen.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
//model class for API and database
@Entity (tableName = "movies")
class Movie {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "id")
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    @Expose
    var popularity: Double? = null

    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    @Expose
    var voteCount: Int? = null

    @ColumnInfo(name = "video")
    @SerializedName("video")
    @Expose
    var video: Boolean? = null

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null

    @ColumnInfo(name = "adult")
    @SerializedName("adult")
    @Expose
    var adult: Boolean? = null

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null

    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null

    @Ignore
    @SerializedName("genre_ids")
    @Expose
    var genreIds: List<Int>? = null

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    var title: String? = null

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    @Expose
    var overview: String? = null

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null

    @SerializedName("revenue")
    @Expose
    var revenue: Int? = null

    var genre1: String? = null
    var genre2: String? = null
    var genre3: String? = null
    var genre4: String? = null
    var genre5: String? = null

    //this converter is required as API gives just 'ids' of genres instead of names
    //I could download names of genres using TMDBService fun getMovieDetails() but it is unnecessary for now
    //as I don't need any more details for now
    fun convertGenres(genreIds: List<Int>){
        lateinit var genre: String
        loop@ for (id in genreIds) {
            genre = when (id) {
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
                else -> continue@loop
            }
            if (genre1 == null) {
                genre1 = genre
            } else if (genre2 == null) {
                genre2 = genre
            } else if (genre3 == null) {
                genre3 = genre
            } else if (genre4 == null) {
                genre4 = genre
            } else if (genre5 == null) {
                genre5 = genre
                break@loop
            }
        }

    }
}