package com.zywczas.bestonscreen.model.localstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface MovieDao {

    @Insert
    fun addMovie(movieFromDB: MovieFromDB)

    //tu dalem flowabl z rxjava 3 - sprawdzic czy nie powinno byc z 2
    @Query ("SELECT * FROM movies")
    fun getMoviesFromDB() : Flowable<List<MovieFromDB>>
}