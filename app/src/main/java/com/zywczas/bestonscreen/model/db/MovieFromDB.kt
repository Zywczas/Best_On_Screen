package com.zywczas.bestonscreen.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieFromDB(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "poster_path", defaultValue = "")
    val posterPath: String,

    @ColumnInfo(name = "title", defaultValue = "")
    val title: String,

    @ColumnInfo(name = "vote_average", defaultValue = "0.0")
    val voteAverage: Double,

    @ColumnInfo(name = "overview", defaultValue = "")
    val overview: String,

    @ColumnInfo(name = "release_date", defaultValue = "")
    val releaseDate: String,

    @ColumnInfo(name = "genre1", defaultValue = "")
    val genre1: String,

    @ColumnInfo(name = "genre2", defaultValue = "")
    val genre2: String,

    @ColumnInfo(name = "genre3", defaultValue = "")
    val genre3: String,

    @ColumnInfo(name = "genre4", defaultValue = "")
    val genre4: String,

    @ColumnInfo(name = "genre5", defaultValue = "")
    val genre5: String,

    @ColumnInfo(name = "genres_amount", defaultValue = "0")
    val assignedGenresAmount: Int
)
