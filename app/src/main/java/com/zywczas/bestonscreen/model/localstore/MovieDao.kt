package com.zywczas.bestonscreen.model.localstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zywczas.bestonscreen.model.Movie
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface MovieDao {

    @Insert
    fun addMovie(movieFromDB: MovieFromDB)

    @Query ("SELECT * FROM movies")
    fun getMovies() : Flowable<List<MovieFromDB>>

    @Query ("SELECT * FROM movies WHERE id == :movieId")
    fun getMovie (movieId: Int) : Observable<MovieFromDB>
}