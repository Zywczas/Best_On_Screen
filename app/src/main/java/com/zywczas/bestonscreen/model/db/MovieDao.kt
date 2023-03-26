package com.zywczas.bestonscreen.model.db

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Throws(SQLiteConstraintException::class)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertMovie(movie: LocalMovie): Single<Long>

    @Delete
    fun deleteMovie(movie: LocalMovie): Single<Int>

    @Query("SELECT * FROM movies ORDER BY title ASC")
    fun getMovies(): Flowable<List<LocalMovie>>

    @Query("SELECT COUNT(id) FROM movies WHERE id ==  :movieId")
    fun getIdCount(movieId: Int): Flowable<Int>

}
