package com.zywczas.bestonscreen.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieFromDB(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "poster_path", defaultValue = "")
    var posterPath: String,

    @ColumnInfo(name = "title", defaultValue = "")
    var title: String,

    @ColumnInfo(name = "vote_average", defaultValue = "0.0")
    var voteAverage: Double,

    @ColumnInfo(name = "overview", defaultValue = "")
    var overview: String,

    @ColumnInfo(name = "release_date", defaultValue = "")
    var releaseDate: String,

    @ColumnInfo(name = "genre1", defaultValue = "")
    var genre1: String,

    @ColumnInfo(name = "genre2", defaultValue = "")
    var genre2: String,

    @ColumnInfo(name = "genre3", defaultValue = "")
    var genre3: String,

    @ColumnInfo(name = "genre4", defaultValue = "")
    var genre4: String,

    @ColumnInfo(name = "genre5", defaultValue = "")
    var genre5: String,

    @ColumnInfo(name = "genres_amount", defaultValue = "0")
    var assignedGenresAmount: Int
)
