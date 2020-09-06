package com.zywczas.bestonscreen.model.db


import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single



@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertMovie(movieFromDB: MovieFromDB) : Single<Long>

    @Delete
    fun deleteMovie(movieFromDB: MovieFromDB) : Single<Int>

    @Query ("SELECT * FROM movies ORDER BY title ASC")
    fun getMovies() : Flowable<List<MovieFromDB>>

    @Query("SELECT COUNT(id) FROM movies WHERE id ==  :movieId")
    fun getIdCount(movieId : Int) : Flowable<Int>

}