package com.zywczas.bestonscreen.model.localstore

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.zywczas.bestonscreen.model.webservice.MovieFromApi

/**
 * Model class for local data base
 */
@Entity (tableName = "movies")
class MovieFromDB() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null

    @ColumnInfo(name = "popularity")
    var popularity: Double? = null

    @ColumnInfo(name = "vote_count")
    var voteCount: Int? = null

    @ColumnInfo(name = "video")
    var video: Boolean? = null

    @ColumnInfo(name = "poster_path")
    var posterPath: String? = null

    @ColumnInfo(name = "adult")
    var adult: Boolean? = null

    @ColumnInfo(name = "backdrop_path")
    var backdropPath: String? = null

    @ColumnInfo(name = "original_language")
    var originalLanguage: String? = null

    @ColumnInfo(name = "original_title")
    var originalTitle: String? = null

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "vote_average")
    var voteAverage: Double? = null

    @ColumnInfo(name = "overview")
    var overview: String? = null

    @ColumnInfo(name = "release_date")
    var releaseDate: String? = null

    @ColumnInfo(name = "genre1")
    var genre1: String? = null

    @ColumnInfo(name = "genre2")
    var genre2: String? = null

    @ColumnInfo(name = "genre3")
    var genre3: String? = null

    @ColumnInfo(name = "genre4")
    var genre4: String? = null

    @ColumnInfo(name = "genre5")
    var genre5: String? = null

    @ColumnInfo(name = "genres_amount")
    var genresAmount: Int = 0

}