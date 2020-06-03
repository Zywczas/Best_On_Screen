package com.zywczas.bestonscreen.model.localstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MovieDao {

    @Insert
    fun addMovie(movieFromDB: MovieFromDB) : Int

    //tu dalem flowabl z rxjava 3 - sprawdzic czy nie powinno byc z 2
    @Query ("SELECT * FROM movies")
    fun getMoviesFromDB() : Flowable<List<MovieFromDB>>
}