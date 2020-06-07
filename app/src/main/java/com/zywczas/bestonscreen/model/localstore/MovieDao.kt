package com.zywczas.bestonscreen.model.localstore

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addMovie(movieFromDB: MovieFromDB) : Completable

    @Delete
    fun deleteMovie(movieFromDB: MovieFromDB) : Completable

    @Query ("SELECT * FROM movies")
    fun getMovies() : Flowable<List<MovieFromDB>>

    @Query ("SELECT * FROM movies WHERE id == :movieId")
    fun getMovie (movieId: Int) : Observable<MovieFromDB>

    /**
     * Listen to changes in data base, if row is created return Int '1', if row is removed return Int '0'
     */
    @Query("SELECT COUNT(id) FROM movies WHERE id ==  :movieId")
    fun checkIfExists(movieId : Int) : Observable<Int>

}