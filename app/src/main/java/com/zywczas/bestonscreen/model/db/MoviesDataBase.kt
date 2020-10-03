package com.zywczas.bestonscreen.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieFromDB::class], version = 2)
abstract class MoviesDataBase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}