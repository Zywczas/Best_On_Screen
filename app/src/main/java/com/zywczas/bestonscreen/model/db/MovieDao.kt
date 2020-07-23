package com.zywczas.bestonscreen.model.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addMovie(movieFromDB: MovieFromDB) : Completable

    @Delete
    fun deleteMovie(movieFromDB: MovieFromDB) : Completable

    @Query ("SELECT * FROM movies ORDER BY title ASC")
    fun getMovies() : Flowable<List<MovieFromDB>>

    @Query("SELECT COUNT(id) FROM movies WHERE id ==  :movieId")
    fun getIdCount(movieId : Int) : Observable<Int>

}