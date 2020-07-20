package com.zywczas.bestonscreen.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "movies")
class MovieFromDB(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?,

    @ColumnInfo(name = "poster_path")
    var posterPath: String?,

    @ColumnInfo(name = "title")
    var title: String?,

    @ColumnInfo(name = "vote_average")
    var voteAverage: Double?,

    @ColumnInfo(name = "overview")
    var overview: String?,

    @ColumnInfo(name = "release_date")
    var releaseDate: String?,

    @ColumnInfo(name = "genre1")
    var genre1: String?,

    @ColumnInfo(name = "genre2")
    var genre2: String?,

    @ColumnInfo(name = "genre3")
    var genre3: String?,

    @ColumnInfo(name = "genre4")
    var genre4: String?,

    @ColumnInfo(name = "genre5")
    var genre5: String?,

    @ColumnInfo(name = "genres_amount")
    var genresAmount: Int?
)
