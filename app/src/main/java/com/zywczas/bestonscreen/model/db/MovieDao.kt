package com.zywczas.bestonscreen.model.db


import androidx.room.*
import com.zywczas.bestonscreen.utilities.Event
import io.reactivex.Flowable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertMovie(movieFromDB: MovieFromDB) : Single<Event<String>>

    @Delete
    fun deleteMovie(movieFromDB: MovieFromDB) : Single<Event<String>>

    //todo pozamieniac na Flowable z RX3 i usunac bridge z gradle
    @Query ("SELECT * FROM movies ORDER BY title ASC")
    fun getMovies() : Flowable<List<MovieFromDB>>

    @Query("SELECT COUNT(id) FROM movies WHERE id ==  :movieId")
    fun getIdCount(movieId : Int) : Flowable<Int>

}