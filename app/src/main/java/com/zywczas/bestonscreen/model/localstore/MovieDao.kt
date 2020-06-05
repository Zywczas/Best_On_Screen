package com.zywczas.bestonscreen.model.localstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Insert
    fun addMovie(movieFromDB: MovieFromDB)

    @Query ("SELECT * FROM movies")
    fun getMovies() : Flowable<List<MovieFromDB>>

    @Query ("SELECT * FROM movies WHERE id == :movieId")
    fun getMovie (movieId: Int) : Single<MovieFromDB>
}